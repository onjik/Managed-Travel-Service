package click.porito.travel_core.security;

import click.porito.travel_core.global.exception.ErrorCode;

public class JwtProcessingServerException extends SecurityServerException{
    public JwtProcessingServerException() {
        super(ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }

    public JwtProcessingServerException(Throwable cause) {
        super(cause, ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }
}
