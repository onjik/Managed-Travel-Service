package click.porito.account.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private String secret;
    private long expirationMin = 60;//기본 1시간
}
