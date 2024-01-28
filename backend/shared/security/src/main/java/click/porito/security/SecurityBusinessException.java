package click.porito.security;


import click.porito.common.exception.Domain;
import click.porito.common.exception.BusinessException;
import click.porito.common.exception.ErrorCodes;

public class SecurityBusinessException extends BusinessException {
    public SecurityBusinessException(ErrorCodes errorCode) {
        super(Domain.SECURITY, errorCode);
    }

    public SecurityBusinessException(Throwable cause, ErrorCodes errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
