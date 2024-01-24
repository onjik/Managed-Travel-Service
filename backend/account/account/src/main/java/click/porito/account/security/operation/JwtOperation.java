package click.porito.account.security.operation;

import click.porito.account.security.JwtProcessingServerException;
import click.porito.account.security.JwtTokenExpiredException;
import click.porito.account.security.JwtTokenInvalidException;
import click.porito.account.security.domain.JwtAuthentication;

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
     * @throws JwtTokenExpiredException if token is expired
     * @throws JwtTokenInvalidException if token is invalid
     */
    JwtAuthentication parseToken(String token);

}
