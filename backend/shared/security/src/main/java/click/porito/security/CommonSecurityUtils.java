package click.porito.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

public class CommonSecurityUtils {
    public static void clearDefaults(HttpSecurity http) throws Exception {
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
}
