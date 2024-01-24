package click.porito.account.security;


import click.porito.account.global.exception.ErrorCode;

public class JwtProcessingServerException extends SecurityServerException{
    public JwtProcessingServerException() {
        super(ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }

    public JwtProcessingServerException(Throwable cause) {
        super(cause, ErrorCode.JWT_PROCESSING_SERVER_FAILED);
    }
}
