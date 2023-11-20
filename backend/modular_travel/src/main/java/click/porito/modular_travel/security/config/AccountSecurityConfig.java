package click.porito.modular_travel.security.config;

import click.porito.modular_travel.security.component.OidcLoginFailureHandler;
import click.porito.modular_travel.security.component.OidcLoginSuccessHandler;
import click.porito.modular_travel.security.filter.RegisterAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration(proxyBeanMethods = true)
@RequiredArgsConstructor
public class AccountSecurityConfig {


    private final OidcLoginFailureHandler oidcLoginFailureHandler;
    private final OidcLoginSuccessHandler oidcLoginSuccessHandler;
    private final RegisterAuthenticationFilter registerAuthenticationFilter;

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
                .requestMatchers("/test").permitAll()
//                .requestMatchers("/playground").permitAll()
                .anyRequest().permitAll()
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
        http.addFilterBefore(registerAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);

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
                .authorizedClientRepository(new HttpSessionOAuth2AuthorizedClientRepository())
                .authorizationEndpoint(authorization -> authorization
                        .baseUri("/account/login/oauth2/authorization")
                        // 인증 시작할 때 사용하는 endpoint
                        //<a href="/account/login/oauth2/authorization/google">Google</a>
                )
                .failureHandler(oidcLoginFailureHandler)
                .successHandler(oidcLoginSuccessHandler)
        );
        return http.build();
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
