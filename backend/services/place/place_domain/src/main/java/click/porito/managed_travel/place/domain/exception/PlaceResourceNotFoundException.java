package click.porito.managed_travel.place.domain.exception;

import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ResourceNotFoundException;

public class PlaceResourceNotFoundException extends ResourceNotFoundException {
    public PlaceResourceNotFoundException() {
        super(Domain.PLACE);
    }
}
