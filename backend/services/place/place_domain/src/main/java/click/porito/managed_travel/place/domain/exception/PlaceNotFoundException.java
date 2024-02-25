package click.porito.managed_travel.place.domain.exception;


import static click.porito.common.exception.ErrorCodes.RESOURCE_NOT_FOUND;

public class PlaceNotFoundException extends PlaceBusinessException {
    public PlaceNotFoundException(String placeId) {
        this(null, placeId);
    }

    public PlaceNotFoundException(Throwable cause, String placeId) {
        super(cause, RESOURCE_NOT_FOUND);
        super.addDetail("placeId", placeId);
    }
}
