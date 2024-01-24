package click.porito.account.security.operation;

import click.porito.account.security.JwtProcessingServerException;
import click.porito.account.security.JwtTokenExpiredException;
import click.porito.account.security.JwtTokenInvalidException;
import click.porito.account.security.config.JwtConfigProperties;
import click.porito.account.security.domain.JwtAuthentication;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;

@Component
@RefreshScope
@RequiredArgsConstructor
public class JwtOperationImpl implements JwtOperation, InitializingBean {
    private final static String ROLES = "roles";
    @Value("${spring.application.name}")
    private String applicationName = "travel_core";

    private final JwtConfigProperties jwtProperties;
    private Algorithm algorithm;
    private JWTVerifier verifier;
    @Override
    public String generateToken(JwtAuthentication payload) {
        Assert.notNull(payload, "payload must not be null");
        Assert.notNull(payload.userId(), "userId must not be null");
        Assert.notEmpty(payload.roles(), "roles must not be empty");
        final String roles = String.join(",", payload.roles());
        Date expiresAt = new Date(System.currentTimeMillis() + jwtProperties.getExpirationMin() * 60 * 1000);
        try {

            return JWT.create()
                    .withExpiresAt(expiresAt)
                    .withSubject(payload.userId())
                    .withClaim(ROLES, roles)
                    .withIssuer(applicationName)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new JwtProcessingServerException(e);
        }
    }

    @Override
    public JwtAuthentication parseToken(String token) {
        Assert.notNull(token, "token must not be null");
        try {
            var decoded = verifier.verify(token);
            var userId = decoded.getSubject();
            String[] roleArray = decoded.getClaim(ROLES).asString().split(",");
            var roles = Set.of(roleArray);
            return new JwtAuthentication(userId, roles);
        } catch (TokenExpiredException e) {
            throw new JwtTokenExpiredException(e);
        } catch (Exception e) {
            throw new JwtTokenInvalidException(e);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("jwt.secret must not be null or empty");
        }
        algorithm = Algorithm.HMAC512(secret);
        verifier = JWT.require(algorithm)
                .build();

    }
}
