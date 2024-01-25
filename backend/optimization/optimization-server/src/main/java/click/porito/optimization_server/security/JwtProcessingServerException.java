package click.porito.optimization_server.security;


import click.porito.optimization_server.global.exception.ErrorCode;

public class JwtProcessingServerException extends SecurityServerException {
    public JwtProcessingServerException() {
        super(ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }

    public JwtProcessingServerException(Throwable cause) {
        super(cause, ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }
}
