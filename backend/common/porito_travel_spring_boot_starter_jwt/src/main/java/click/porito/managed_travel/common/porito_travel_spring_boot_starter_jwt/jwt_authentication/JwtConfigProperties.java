package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
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