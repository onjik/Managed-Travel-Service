package click.porito.common.exception.common;

import click.porito.common.exception.Domain;
import click.porito.common.exception.BusinessException;
import click.porito.common.exception.ErrorCodes;

public abstract class CommonBusinessException extends BusinessException {
    public CommonBusinessException(Domain domain, ErrorCodes errorCode) {
        super(domain, errorCode);
    }

    public CommonBusinessException(Throwable cause, Domain domain, ErrorCodes errorCode) {
        super(cause, domain, errorCode);
    }
}
