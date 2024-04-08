package click.porito.common.exception.common;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;

public class AccessDeniedException extends CommonBusinessException{
    public AccessDeniedException(Domain domain) {
        super(domain, ErrorCodes.ACCESS_DENIED);
    }

    public AccessDeniedException(Throwable cause, Domain domain) {
        super(cause, domain, ErrorCodes.ACCESS_DENIED);
    }
}
