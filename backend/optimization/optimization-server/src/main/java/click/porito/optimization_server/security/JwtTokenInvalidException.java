package click.porito.optimization_server.security;


import click.porito.optimization_server.global.exception.ErrorCode;

public class JwtTokenInvalidException extends SecurityBusinessException {
    public JwtTokenInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }

    public JwtTokenInvalidException(Throwable cause) {
        super(cause, ErrorCode.JWT_INVALID);
    }
}
