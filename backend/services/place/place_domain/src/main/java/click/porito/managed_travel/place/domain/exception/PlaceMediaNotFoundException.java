package click.porito.managed_travel.place.domain.exception;

public class PlaceMediaNotFoundException extends PlaceResourceNotFoundException{
    public static final String MEDIA_ID_DETAIL_KEY = "mediaId";
    public PlaceMediaNotFoundException(Throwable cause, String mediaId) {
        super(cause);
        super.addDetail(MEDIA_ID_DETAIL_KEY, mediaId);
    }
}
