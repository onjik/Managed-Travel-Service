package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCodes;
import click.porito.security.SecurityBusinessException;

public class JwtTokenInvalidException extends SecurityBusinessException {
    public JwtTokenInvalidException() {
        super(ErrorCodes.JWT_INVALID);
    }

    public JwtTokenInvalidException(Throwable cause) {
        super(cause, ErrorCodes.JWT_INVALID);
    }
}
