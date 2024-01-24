package click.porito.account.security;


import click.porito.account.global.exception.ErrorCode;

public class JwtTokenExpiredException extends SecurityBusinessException {
    public JwtTokenExpiredException() {
        super(ErrorCode.JWT_EXPIRED);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCode.JWT_EXPIRED);
    }
}
