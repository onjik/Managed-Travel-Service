package click.porito.place_service.google_api;

public interface GooglePlacePhotoApi {
    String photoUri(String photoReference, int maxWidthPx, int maxHeightPx);
}
