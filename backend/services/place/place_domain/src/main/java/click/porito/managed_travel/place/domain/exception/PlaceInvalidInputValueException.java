package click.porito.managed_travel.place.domain.exception;

import click.porito.common.exception.Domain;
import click.porito.common.exception.common.InvalidInputValueException;

import java.util.List;

public class PlaceInvalidInputValueException extends InvalidInputValueException {
    public PlaceInvalidInputValueException(List<String> invalidFields) {
        super(Domain.PLACE, invalidFields);
    }

    public PlaceInvalidInputValueException(Throwable cause, List<String> invalidFields) {
        super(cause, Domain.PLACE, invalidFields);
    }
}
