package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.autoconfigure;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.JwtOperationAutoConfiguration;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication.JwtOperation;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.CommonSecurityUtils;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.ForbiddenAccessDeniedHandler;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.UnauthorizedAuthenticationEntryPoint;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.filter.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration(after = {JwtOperationAutoConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({JwtOperationAutoConfiguration.class})
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityChainAutoConfiguration {
    @Bean
    @ConditionalOnDefaultWebSecurity
    public SecurityFilterChain config(
            HttpSecurity http,
            ApplicationEventPublisher applicationEventPublisher,
            JwtOperation jwtOperation
    ) throws Exception {
        // disable unnecessary security features
        CommonSecurityUtils.clearDefaults(http);
        // exception handling
        http.exceptionHandling(
                exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint()) // on AuthenticationException, respons with 401
                        .accessDeniedHandler(new ForbiddenAccessDeniedHandler()) // on AccessDeniedException, response with 403
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
}
