package click.porito.security.jwt_authentication;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties implements InitializingBean {
    private String secret;
    private long expirationMin = 60;//기본 1시간

    @Override
    public void afterPropertiesSet() throws Exception {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("jwt.secret is required");
        }
    }
}