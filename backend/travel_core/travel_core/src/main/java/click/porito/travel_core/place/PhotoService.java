package click.porito.travel_core.place;

public interface PhotoService {
    String getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight);
}
