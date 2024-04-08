package click.porito.common.exception.common;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;

public class UnexpectedServerException extends CommonServerException{
    public UnexpectedServerException(Domain domain) {
        super(domain, ErrorCodes.UNEXPECTED_SERVER_ERROR);
    }

    public UnexpectedServerException(Throwable cause, Domain domain) {
        super(cause, domain, ErrorCodes.UNEXPECTED_SERVER_ERROR);
    }
}
