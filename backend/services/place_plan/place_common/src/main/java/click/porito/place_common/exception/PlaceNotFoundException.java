package click.porito.place_common.exception;


import static click.porito.common.exception.ErrorCode.RESOURCE_NOT_FOUND;

public class PlaceNotFoundException extends PlaceBusinessException {
    public PlaceNotFoundException(String placeId) {
        this(null, placeId);
    }

    public PlaceNotFoundException(Throwable cause, String placeId) {
        super(cause, RESOURCE_NOT_FOUND);
        super.addDetail("placeId", placeId);
    }
}
