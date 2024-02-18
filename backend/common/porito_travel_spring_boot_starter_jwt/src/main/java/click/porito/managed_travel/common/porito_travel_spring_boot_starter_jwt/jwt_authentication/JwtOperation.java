package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication;

import click.porito.common.security.UserContext;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception.JwtProcessingServerException;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception.JwtTokenExpiredException;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception.JwtTokenInvalidException;

import java.util.List;

public interface JwtOperation {

    /**
     * Generate JWT token
     * @param userContext user context
     * @return JWT token
     * @throws JwtProcessingServerException if failed to generate token
     */
    String generateToken(UserContext userContext);

    default String generateToken(String userId, List<String> roles) {
        return generateToken(new UserContext(userId, roles));
    }

    /**
     * Parse JWT token
     * @param token JWT token
     * @return JwtAuthentication
     * @throws JwtProcessingServerException if failed to parse token
     * @throws JwtTokenExpiredException if token is expired
     * @throws JwtTokenInvalidException if token is invalid
     */
    UserContext parseToken(String token);

}
