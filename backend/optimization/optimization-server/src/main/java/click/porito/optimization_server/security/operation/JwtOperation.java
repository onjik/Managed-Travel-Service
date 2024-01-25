package click.porito.optimization_server.security.operation;

import click.porito.optimization_server.security.JwtProcessingServerException;
import click.porito.optimization_server.security.JwtTokenExpiredException;
import click.porito.optimization_server.security.JwtTokenInvalidException;
import click.porito.optimization_server.security.domain.JwtAuthentication;

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
