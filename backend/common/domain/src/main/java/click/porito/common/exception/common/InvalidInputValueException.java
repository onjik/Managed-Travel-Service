package click.porito.common.exception.common;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;

public class InvalidInputValueException extends BusinessException {
    public InvalidInputValueException(Domain domain) {
        super(domain, ErrorCodes.INVALID_INPUT_VALUE);
    }

    public InvalidInputValueException(Throwable cause, Domain domain) {
        super(cause, domain, ErrorCodes.INVALID_INPUT_VALUE);
    }
}
