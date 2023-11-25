package click.porito.modular_travel.security.service;

import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.security.dto.JwtPayload;
import click.porito.modular_travel.security.exception.JwtEncodeException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService implements InitializingBean {
    private final static String ROLES = "roles";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationMin}")
    private long expirationMin;
    private final ObjectMapper objectMapper;
    private Algorithm algorithm;
    private JWTVerifier verifier;


    @Override
    public void afterPropertiesSet() throws Exception {
        algorithm = Algorithm.HMAC512(secret);
        verifier = JWT.require(algorithm)
                .build();
    }

    /**
     * @return jwt token
     * @throws JwtEncodeException when fail to encode
     */
    public String encode(JwtPayload payload)  {
        Assert.notNull(payload, "payload must not be null");
        Assert.notNull(payload.userId(), "userId must not be null");
        Assert.notEmpty(payload.roles(), "roles must not be empty");
        final String roles;
        try {
            roles = objectMapper.writeValueAsString(payload.roles());
        } catch (JsonProcessingException e) {
            throw new JwtEncodeException();
        }
        Date expiresAt = new Date(System.currentTimeMillis() + expirationMin * 60 * 1000);
        return JWT.create()
                .withExpiresAt(expiresAt)
                .withSubject(payload.userId())
                .withClaim(ROLES, roles)
                .withIssuer("account_service")
                .sign(algorithm);
    }

    public String encodeRegisterDTO(AccountRegisterDTO registerDTO){
        final String accountRegisterDTOJson;
        try {
            accountRegisterDTOJson = objectMapper.writeValueAsString(registerDTO);
        } catch (JsonProcessingException e) {
            throw new JwtEncodeException();
        }
        return JWT.create()
                .withClaim("registerDTO", accountRegisterDTOJson)
                .sign(algorithm);
    }

    /**
     * @param token to verify.
     * @return a verified and decoded JWT.
     * @throws AlgorithmMismatchException     if the algorithm stated in the token's header it's not equal to the one defined in the {@link JWTVerifier}.
     * @throws SignatureVerificationException if the signature is invalid.
     * @throws TokenExpiredException          if the token has expired.
     * @throws InvalidClaimException          if a claim contained a different value than the expected one.
     */
    public AccountRegisterDTO decodeRegisterDTO(String token){
        String registerDTOJson = verifier
                .verify(token)
                .getClaim("registerDTO")
                .asString();
        try {
            return objectMapper.readValue(registerDTOJson, AccountRegisterDTO.class);
        } catch (JsonProcessingException e) {
            throw new JWTDecodeException("fail to decode registerDTO", e);
        }
    }


}
