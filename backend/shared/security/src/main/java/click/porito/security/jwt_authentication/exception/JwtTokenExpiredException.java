package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCodes;
import click.porito.security.SecurityBusinessException;

public class JwtTokenExpiredException extends SecurityBusinessException {
    public JwtTokenExpiredException() {
        super(ErrorCodes.JWT_EXPIRED);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCodes.JWT_EXPIRED);
    }
}
