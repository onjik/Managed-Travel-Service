package click.porito.common.exception.common;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCode;
import click.porito.common.exception.ServerException;

public abstract class CommonServerException extends ServerException {
    public CommonServerException(Domain domain, ErrorCode errorCode) {
        super(domain, errorCode);
    }

    public CommonServerException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
