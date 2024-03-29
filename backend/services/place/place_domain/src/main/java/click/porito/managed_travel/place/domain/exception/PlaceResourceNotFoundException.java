package click.porito.managed_travel.place.domain.exception;

import click.porito.common.exception.ErrorCodes;

public class PlaceResourceNotFoundException extends PlaceBusinessException {

    public PlaceResourceNotFoundException() {
        super(ErrorCodes.RESOURCE_NOT_FOUND);
    }

    public PlaceResourceNotFoundException(Throwable cause) {
        super(cause, ErrorCodes.RESOURCE_NOT_FOUND);

    }
}
