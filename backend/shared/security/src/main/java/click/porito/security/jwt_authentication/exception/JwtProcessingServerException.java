package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCode;
import click.porito.security.SecurityServerException;

public class JwtProcessingServerException extends SecurityServerException {
    public JwtProcessingServerException() {
        super(ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }

    public JwtProcessingServerException(Throwable cause) {
        super(cause, ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }
}
