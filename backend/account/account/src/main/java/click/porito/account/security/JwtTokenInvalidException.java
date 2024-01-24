package click.porito.account.security;


import click.porito.account.global.exception.ErrorCode;

public class JwtTokenInvalidException extends SecurityBusinessException {
    public JwtTokenInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }

    public JwtTokenInvalidException(Throwable cause) {
        super(cause, ErrorCode.JWT_INVALID);
    }
}
