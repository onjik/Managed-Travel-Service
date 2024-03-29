package click.porito.managed_travel.place.domain.exception;


public class PlaceNotFoundException extends PlaceResourceNotFoundException {
    public PlaceNotFoundException(String placeId) {
        this(null, placeId);
    }

    public PlaceNotFoundException(Throwable cause, String placeId) {
        super(cause);
        super.addDetail("placeId", placeId);
    }
}
