package click.porito.place_service;

public interface PhotoService {
    String getPhotoUrl(String placeId, String photoId, int maxWidth, int maxHeight);
}
