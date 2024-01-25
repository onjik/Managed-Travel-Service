package click.porito.optimization_server.security;


import click.porito.optimization_server.global.exception.ErrorCode;

public class JwtTokenExpiredException extends SecurityBusinessException {
    public JwtTokenExpiredException() {
        super(ErrorCode.JWT_EXPIRED);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCode.JWT_EXPIRED);
    }
}
