package click.porito.travel_core.config;

import click.porito.travel_core.security.XHeaderAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain config(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
        // disable unnecessary security features
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
                .oidcLogout(c -> c.disable())
                .requestCache(c -> c.disable())
                .headers(c -> c.disable());

        return http
                .authorizeHttpRequests(
                        c -> c.anyRequest().authenticated()
                )
                .addFilterAfter(new XHeaderAuthenticationFilter(objectMapper), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
