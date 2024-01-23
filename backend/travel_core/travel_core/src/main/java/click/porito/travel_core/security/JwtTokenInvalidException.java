package click.porito.travel_core.security;

import click.porito.travel_core.global.exception.ErrorCode;

public class JwtTokenInvalidException extends SecurityBusinessException{
    public JwtTokenInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }

    public JwtTokenInvalidException(Throwable cause) {
        super(cause, ErrorCode.JWT_INVALID);
    }
}
