package click.porito.common.exception.common;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

public class InvalidInputValueException extends BusinessException {
    private final List<String> invalidFields = new ArrayList<>();
    public InvalidInputValueException(Domain domain, List<String> invalidFields) {
        super(domain, ErrorCodes.INVALID_INPUT_VALUE);
        if (invalidFields != null) {
            this.invalidFields.addAll(invalidFields);
        }
    }

    public InvalidInputValueException(Throwable cause, Domain domain, List<String> invalidFields) {
        super(cause, domain, ErrorCodes.INVALID_INPUT_VALUE);
        if (invalidFields != null) {
            this.invalidFields.addAll(invalidFields);
        }
    }
}
