package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCode;
import click.porito.security.SecurityBusinessException;

public class JwtTokenInvalidException extends SecurityBusinessException {
    public JwtTokenInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }

    public JwtTokenInvalidException(Throwable cause) {
        super(cause, ErrorCode.JWT_INVALID);
    }
}
