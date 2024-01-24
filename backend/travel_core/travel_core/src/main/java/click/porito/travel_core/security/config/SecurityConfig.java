package click.porito.travel_core.security.config;

import click.porito.travel_core.global.exception.ErrorResponseBody;
import click.porito.travel_core.security.filter.JwtAuthenticationFilter;
import click.porito.travel_core.security.operation.JwtOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        //{domain}:{action}:{scope}
        //domain : place, plan
        //action : read, create, update, delete
        //scope : owned, belonged, all, new(only for *:create)
        String hierarchyString = """
                ROLE_ADMIN > ROLE_STAFF
                ROLE_STAFF > ROLE_USER
                ROLE_STAFF > place:read:all
                ROLE_STAFF > plan:read:all
                ROLE_USER > place:read:all
                ROLE_USER > plan:create:owned
                ROLE_USER > plan:read:owned
                ROLE_USER > plan:read:belonged
                ROLE_USER > plan:update:owned
                ROLE_USER > plan:update:belonged
                ROLE_USER > plan:delete:owned
                """;
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(hierarchyString);
        return hierarchy;
    }

    //for method security
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
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
        // authentication, authorization
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtOperation);
        if (applicationEventPublisher != null) {
            jwtAuthenticationFilter.setApplicationEventPublisher(applicationEventPublisher);
        }
        return http
                .authorizeHttpRequests(
                        c -> c.anyRequest().permitAll()
                )
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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

}
