package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCode;
import click.porito.security.SecurityBusinessException;

public class JwtTokenExpiredException extends SecurityBusinessException {
    public JwtTokenExpiredException() {
        super(ErrorCode.JWT_EXPIRED);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCode.JWT_EXPIRED);
    }
}
