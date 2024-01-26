package click.porito.security.jwt_authentication;

import click.porito.security.jwt_authentication.exception.JwtProcessingServerException;
import click.porito.security.jwt_authentication.exception.JwtTokenExpiredException;
import click.porito.security.jwt_authentication.exception.JwtTokenInvalidException;

import java.util.List;

public interface JwtOperation {

    /**
     * Generate JWT token
     * @param userId user id
     * @param authorities authorities
     * @return JWT token
     * @throws JwtProcessingServerException if failed to generate token
     */
    String generateToken(String userId, List<String> authorities);

    /**
     * Parse JWT token
     * @param token JWT token
     * @return JwtAuthentication
     * @throws JwtProcessingServerException if failed to parse token
     * @throws JwtTokenExpiredException if token is expired
     * @throws JwtTokenInvalidException if token is invalid
     */
    JwtAuthentication parseToken(String token);

}
