package click.porito.travel_core.place;

import static click.porito.travel_core.global.exception.ErrorCode.RESOURCE_NOT_FOUND;

public class PlaceNotFoundException extends PlaceBusinessException{
    public PlaceNotFoundException() {
        super(RESOURCE_NOT_FOUND);
    }

    public PlaceNotFoundException(Throwable cause) {
        super(cause, RESOURCE_NOT_FOUND);
    }
}
