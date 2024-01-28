package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCodes;
import click.porito.security.SecurityServerException;

public class JwtProcessingServerException extends SecurityServerException {
    public JwtProcessingServerException() {
        super(ErrorCodes.JWT_PROCESSING_SERVER_FAILED);
    }

    public JwtProcessingServerException(Throwable cause) {
        super(cause, ErrorCodes.JWT_PROCESSING_SERVER_FAILED);
    }
}
