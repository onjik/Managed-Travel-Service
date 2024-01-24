package click.porito.account.security.config;

import click.porito.account.account.operation.AccountOperation;
import click.porito.account.account.policy.AccountAccessPolicy;
import click.porito.account.global.exception.ErrorResponseBody;
import click.porito.account.security.component.DefaultLoginFailureHandler;
import click.porito.account.security.component.DefaultLoginSuccessHandler;
import click.porito.account.security.filter.JwtAuthenticationFilter;
import click.porito.account.security.filter.JwtResponseReinforceFilter;
import click.porito.account.security.operation.JwtOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final DefaultLoginFailureHandler failureHandler;
    private final DefaultLoginSuccessHandler successHandler;
    @Bean
    public RoleHierarchy roleHierarchy() {
        //{domain}:{action}:{scope}
        //domain : place, plan
        //action : read, create, update, delete
        //scope : owned, belonged, all, new(only for *:create)
        String hierarchyString = """
                ROLE_ADMIN > ROLE_STAFF
                ROLE_ADMIN > account:delete:all
                ROLE_ADMIN > account:update:all
                ROLE_ADMIN > account:create:all
                ROLE_ADMIN > ROLE_SERVER
                ROLE_ADMIN > ROLE_STAFF
                ROLE_SERVER > account:read:all
                ROLE_SERVER > ROLE_STAFF
                ROLE_STAFF > ROLE_USER
                ROLE_USER > account:read:owned
                ROLE_USER > account:update:owned
                ROLE_USER > account:delete:owned
                ROLE_USER > account:create:new
                ROLE_USER > account:read:summary
                ROLE_ANONYMOUS > account:create:new
                """;
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(hierarchyString);
        return hierarchy;
    }

    //for method security
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy,
            AccountOperation accountOperation
    ) {
        AccountAccessPolicy accountAccessPolicy = new AccountAccessPolicy(roleHierarchy, accountOperation);
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        expressionHandler.setPermissionEvaluator(accountAccessPolicy);
        return expressionHandler;
    }


    @Bean
    public SecurityFilterChain config(
            HttpSecurity http,
            ApplicationEventPublisher applicationEventPublisher,
            JwtOperation jwtOperation
    ) throws Exception {
        // disable unnecessary security features
        disableUnnecessarySecurityFeatures(http);
        // exception handling
        http.exceptionHandling(
                exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(responseUnauthorized()) // on AuthenticationException, respons with 401
                        .accessDeniedHandler(responseForbidden()) // on AccessDeniedException, response with 403
        );

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




        // authentication, authorization
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtOperation);
        JwtResponseReinforceFilter jwtResponseReinforceFilter = new JwtResponseReinforceFilter(jwtOperation);
        if (applicationEventPublisher != null) {
            jwtAuthenticationFilter.setApplicationEventPublisher(applicationEventPublisher);
        }
        return http
                .authorizeHttpRequests(
                        c -> c.anyRequest().permitAll() // method security 로 대체
                )
                .addFilterBefore(jwtResponseReinforceFilter, OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
                .build();
    }

    private void disableUnnecessarySecurityFeatures(HttpSecurity http) throws Exception {
        http
                .formLogin(c -> c.disable())
                .csrf(c -> c.disable())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    session.sessionCreationPolicy(SessionCreationPolicy.NEVER);
                })
                .cors(c -> c.disable())
                .rememberMe(c -> c.disable())
                .httpBasic(c -> c.disable())
                .logout(c -> c.disable())
                .requestCache(c -> c.disable())
                .headers(c -> c.disable());
    }

    private static final String unauthorizedBody;
    private static final String forbiddenBody;

    static {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            unauthorizedBody = objectMapper.writeValueAsString(
                    ErrorResponseBody.builder()
                            .status(HttpServletResponse.SC_UNAUTHORIZED)
                            .message("Unauthorized")
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            forbiddenBody = objectMapper.writeValueAsString(
                    ErrorResponseBody.builder()
                            .status(HttpServletResponse.SC_FORBIDDEN)
                            .message("Forbidden")
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected AuthenticationEntryPoint responseUnauthorized() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(unauthorizedBody);
        };
    }

    protected AccessDeniedHandler responseForbidden() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(forbiddenBody);
        };
    }

    static class NoContentLogoutSuccessHandler implements LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

}
