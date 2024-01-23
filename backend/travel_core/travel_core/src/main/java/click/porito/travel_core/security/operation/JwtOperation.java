package click.porito.travel_core.security.operation;

import click.porito.travel_core.security.JwtProcessingServerException;
import click.porito.travel_core.security.domain.JwtAuthentication;

public interface JwtOperation {

    /**
     * Generate JWT token
     * @param payload JwtAuthentication
     * @return JWT token
     * @throws JwtProcessingServerException if failed to generate token
     */
    String generateToken(JwtAuthentication payload);

    /**
     * Parse JWT token
     * @param token JWT token
     * @return JwtAuthentication
     * @throws JwtProcessingServerException if failed to parse token
     * @throws click.porito.travel_core.security.JwtTokenExpiredException if token is expired
     * @throws click.porito.travel_core.security.JwtTokenInvalidException if token is invalid
     */
    JwtAuthentication parseToken(String token);

}
