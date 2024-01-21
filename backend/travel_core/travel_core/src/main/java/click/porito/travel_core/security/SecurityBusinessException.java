package click.porito.travel_core.security;

import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.global.exception.BusinessException;
import click.porito.travel_core.global.exception.ErrorCode;

public class SecurityBusinessException extends BusinessException {
    public SecurityBusinessException(ErrorCode errorCode) {
        super(Domain.SECURITY, errorCode);
    }

    public SecurityBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
