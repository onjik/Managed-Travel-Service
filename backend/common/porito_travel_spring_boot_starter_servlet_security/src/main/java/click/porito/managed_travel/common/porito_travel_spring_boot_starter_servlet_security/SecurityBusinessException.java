package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security;


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
