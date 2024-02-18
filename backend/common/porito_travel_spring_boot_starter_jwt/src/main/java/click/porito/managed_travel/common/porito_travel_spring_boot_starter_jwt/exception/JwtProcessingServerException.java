package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception;


import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;
import click.porito.common.exception.ServerException;

public class JwtProcessingServerException extends ServerException {
    public JwtProcessingServerException() {
        super(Domain.SECURITY, ErrorCodes.JWT_PROCESSING_SERVER_FAILED);
    }

    public JwtProcessingServerException(Throwable cause) {
        super(cause, Domain.PLAN, ErrorCodes.JWT_PROCESSING_SERVER_FAILED);
    }
}
