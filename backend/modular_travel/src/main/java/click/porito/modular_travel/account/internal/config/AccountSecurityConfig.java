package click.porito.modular_travel.account.internal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import click.porito.modular_travel.account.internal.dto.LoginSuccessResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.AccountServerException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;
import click.porito.modular_travel.account.internal.service.CustomOidcUserService;

import java.io.IOException;
import java.io.PrintWriter;

import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.IS_NEW_ACCOUNT;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.USER_ID;
import static click.porito.modular_travel.account.internal.exception.AccountErrorCode.INVALID_OIDC_USER_DETECTED;

@Configuration(proxyBeanMethods = true)
@RequiredArgsConstructor
public class AccountSecurityConfig {

    private final AccountRepository accountRepository;


    @Bean
    SecurityFilterChain accountSecurityFilterChain(HttpSecurity http) throws Exception {
        //csrf
//        http.csrf(csrf -> {csrf
//                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
//                .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler());
//        });
        //TODO : csrf 필요시 설정
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
        );
        //프론트와 분리된 API 서버 이므로, request cache 를 사용하지 않는다.
        http.requestCache(RequestCacheConfigurer::disable);

        //exception handling
        http.exceptionHandling(exceptionHandling -> {exceptionHandling
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .accessDeniedHandler(new ForbiddenAccessDeniedHandler());
        });

        //disable other login
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        //remember me
        //TODO : remember me 필요시 설정
        http.rememberMe(AbstractHttpConfigurer::disable);

        //logout
        http.logout(logout -> {
            HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES));
            logout
                    .addLogoutHandler(clearSiteData)
                    .logoutUrl("/account/logout")
                    .logoutSuccessHandler(new NoContentLogoutSuccessHandler())
                    .clearAuthentication(true);
        });


        //oauth2
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .oidcUserService(new CustomOidcUserService(accountRepository)))
                .authorizedClientRepository(new HttpSessionOAuth2AuthorizedClientRepository())
                .authorizationEndpoint(authorization -> authorization
                        .baseUri("/account/login/oauth2/authorization")
                        // 인증 시작할 때 사용하는 endpoint
                        //<a href="/account/login/oauth2/authorization/google">Google</a>
                )
                .failureHandler(new OidcLoginFailureHandler())
                .successHandler(new OidcLoginSuccessHandler(accountRepository))
        );
        return http.build();
    }

    public static class AuthenticationManagerPostProcessor implements ObjectPostProcessor<AuthenticationManager>{

        @Override
        public <O extends AuthenticationManager> O postProcess(O object) {
            return null;
        }
    }



    static class NoContentLogoutSuccessHandler implements LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }


    public static class ForbiddenAccessDeniedHandler implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"message\":\"No Permission\"}");
            out.flush();
        }
    }

    public static class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"message\":\"Unauthorized\"}");
            out.flush();
        }
    }

    public static class OidcLoginFailureHandler implements AuthenticationFailureHandler {
        public static final String INSUFFICIENT_ACCOUNT_INFO = "INSUFFICIENT_ACCOUNT_INFO";
        public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            //그 외의 인증 예외시
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\":\"authentication fail\"}");
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class OidcLoginSuccessHandler implements AuthenticationSuccessHandler {

        private final AccountRepository accountRepository;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OidcUser oidcUser) {
                Boolean isNewAccount = (Boolean) oidcUser.getAttribute(IS_NEW_ACCOUNT);
                Long userId = (Long) oidcUser.getAttribute(USER_ID);
                //Custom Field Check
                if (isNewAccount == null || userId == null) {
                    log.error("Custom Field Missing(userId : {}, isNewAccount : {})", userId, isNewAccount);
                    SecurityContextHolder.clearContext(); //인증 정보를 만료시킨다.
                    throw new AccountServerException(INVALID_OIDC_USER_DETECTED, "Custom Field Missing");
                }
                //Find Account
                Account account = accountRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.error("Oidc Login Success But Unable To Find Account : Account(userId : {}", userId);
                            SecurityContextHolder.clearContext(); //인증 정보를 만료시킨다.
                            return new AccountServerException(INVALID_OIDC_USER_DETECTED, "Unable To Find Account");
                        });
                log.info("Oidc Login Success: Account(userId : {}", userId);
                responseLoginSuccess(response, account, isNewAccount);
            } else {
                //그 외의 인증 결과 - 아직 지원하지 않음
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"message\":\"unknown authentication\"}");
            }
        }


        private void responseLoginSuccess(HttpServletResponse response, Account account, boolean isNewAccount) throws IOException {
            LoginSuccessResponse responseBody = LoginSuccessResponse.from(account, isNewAccount);
            response.setContentType("application/json");
            response.setStatus(200);
            OidcLoginFailureHandler.OBJECT_MAPPER.writeValue(response.getOutputStream(), responseBody);
        }



    }
}
