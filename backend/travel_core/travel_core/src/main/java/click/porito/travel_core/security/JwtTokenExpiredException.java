package click.porito.travel_core.security;

import click.porito.travel_core.global.exception.ErrorCode;

public class JwtTokenExpiredException extends SecurityBusinessException {
    public JwtTokenExpiredException() {
        super(ErrorCode.JWT_EXPIRED);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCode.JWT_EXPIRED);
    }
}
