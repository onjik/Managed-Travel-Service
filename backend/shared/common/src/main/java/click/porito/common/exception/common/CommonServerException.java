package click.porito.common.exception.common;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;
import click.porito.common.exception.ServerException;

public abstract class CommonServerException extends ServerException {
    public CommonServerException(Domain domain, ErrorCodes errorCode) {
        super(domain, errorCode);
    }

    public CommonServerException(Throwable cause, Domain domain, ErrorCodes errorCode) {
        super(cause, domain, errorCode);
    }
}
