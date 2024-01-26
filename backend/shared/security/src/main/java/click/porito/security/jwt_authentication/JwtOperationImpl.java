package click.porito.security.jwt_authentication;

import click.porito.security.jwt_authentication.exception.JwtProcessingServerException;
import click.porito.security.jwt_authentication.exception.JwtTokenExpiredException;
import click.porito.security.jwt_authentication.exception.JwtTokenInvalidException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static click.porito.security.jwt_authentication.JwtConstant.JWT_AUTHORIZATION_KEY;


@RequiredArgsConstructor
public class JwtOperationImpl implements JwtOperation, InitializingBean {
    @Value("${spring.application.name}")
    private String applicationName;

    private final JwtConfigProperties jwtProperties;
    private Algorithm algorithm;
    private JWTVerifier verifier;
    @Override
    public String generateToken(String userId, List<String> authorities) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(authorities, "authorities must not be null");
        Date expiresAt = new Date(System.currentTimeMillis() + jwtProperties.getExpirationMin() * 60 * 1000);
        try {

            return JWT.create()
                    .withExpiresAt(expiresAt)
                    .withSubject(userId)
                    .withClaim(JWT_AUTHORIZATION_KEY, authorities)
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
            String[] roleArray = decoded.getClaim(JWT_AUTHORIZATION_KEY).asString().split(",");
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

        if (applicationName == null || applicationName.isEmpty()) {
            throw new IllegalArgumentException("spring.application.name must not be null or empty");
        }

    }
}
