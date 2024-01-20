package click.porito.travel_core.place;

import static click.porito.travel_core.global.exception.ErrorCode.RESOURCE_NOT_FOUND;

public class PlaceNotFoundException extends PlaceBusinessException{
    public PlaceNotFoundException(String placeId) {
        this(null, placeId);
    }

    public PlaceNotFoundException(Throwable cause, String placeId) {
        super(cause, RESOURCE_NOT_FOUND);
        super.addDetail("placeId", placeId);
    }
}
