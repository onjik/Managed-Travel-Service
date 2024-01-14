package click.porito.gateway.service;

import click.porito.gateway.config.JwtConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService implements InitializingBean {
    private final static String ROLES = "roles";
    private final JwtConfig jwtConfig;

    private JavaType LIST_OF_STRING;
    private JWTVerifier verifier;
    private final ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jwtConfig.getSecret() == null) {
            throw new IllegalArgumentException("jwt secret is null");
        }
        Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecret());
        this.verifier = JWT.require(algorithm).acceptLeeway(5).build();
        this.LIST_OF_STRING = TypeFactory.defaultInstance().constructCollectionLikeType(List.class, String.class);
    }

    public boolean isValid(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return token class, never null
     * @throws JWTDecodeException decode 실패시 발생
     */
    public JwtPayLoad decode(String token) {
        verifier.verify(token);
        DecodedJWT jwt = JWT.decode(token);

        String id = jwt.getSubject();
        String roleJsonList = jwt.getClaim(ROLES).asString();
        final List<String> roles;
        try {
            roles = objectMapper.readValue(roleJsonList, LIST_OF_STRING);
        } catch (JsonProcessingException e) {
            throw new JWTDecodeException("Failed to parse roles", e);
        }
        return new JwtPayLoad(id, roles);
    }

}
