package click.porito.account.security.config;

import click.porito.account.account.AccountService;
import click.porito.account.security.component.DefaultLoginFailureHandler;
import click.porito.account.security.component.DefaultLoginSuccessHandler;
import click.porito.account.security.filter.AuthorizationHeaderTranslationFilter;
import click.porito.account.security.filter.JwtPublishFilter;
import click.porito.account.security.filter.SupplementRegisterAuthenticationFilter;
import click.porito.account.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.io.IOException;
import java.io.PrintWriter;

@EnableWebSecurity
@Configuration(proxyBeanMethods = true)
@RequiredArgsConstructor
public class AccountSecurityConfig {


    private final DefaultLoginFailureHandler failureHandler;
    private final DefaultLoginSuccessHandler successHandler;

    @Bean
    SecurityFilterChain accountSecurityFilterChain(HttpSecurity http,
                                                   JwtService jwtService,
                                                   AccountService accountService,
                                                   Validator validator,
                                                   ObjectMapper objectMapper,
                                                   OAuth2AuthorizedClientService authorizedClientService
                                                   ) throws Exception {
        //csrf
//        http.csrf(csrf -> {csrf
//                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
//                .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler());
//        });
        http.sessionManagement(sessionManagement -> {
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

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
        SupplementRegisterAuthenticationFilter supplementRegisterAuthenticationFilter = new SupplementRegisterAuthenticationFilter(validator, objectMapper, accountService, jwtService, failureHandler, successHandler);
        AuthorizationHeaderTranslationFilter authorizationHeaderTranslationFilter = new AuthorizationHeaderTranslationFilter(objectMapper);
        JwtPublishFilter jwtPublishFilter = new JwtPublishFilter(jwtService);
        /*
        AuthorizationHeaderTranslationFilter
        JwtPublishFilter (여기서 만약 인증이 안되어 있으면, 응답시 jwt를 실어서 보내준다.)
        SupplementRegisterAuthenticationFilter
        OAuth2LoginAuthenticationFilter
         */
        http.addFilterAfter(authorizationHeaderTranslationFilter, OAuth2AuthorizationRequestRedirectFilter.class);
        http.addFilterBefore(jwtPublishFilter, OAuth2LoginAuthenticationFilter.class);
        http.addFilterBefore(supplementRegisterAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);
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
                .authorizationEndpoint(authorization -> authorization
                        .baseUri("/account/login/oauth2/authorization")
                        // 인증 시작할 때 사용하는 endpoint
                        //<a href="/account/login/oauth2/authorization/google">Google</a>
                )
                .failureHandler(failureHandler)
                .successHandler(successHandler)
        );
        return http.build();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            JdbcOperations jdbcOperations,
            ClientRegistrationRepository clientRegistrationRepository // google, facebook, kakao 등의 client 정보를 가지고 있다.
    ) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
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

}
