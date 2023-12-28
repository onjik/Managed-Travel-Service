package click.porito.place_service.model;

public interface PlaceDto {
    String getId();
    String getName();
    String[] getTags();
    String getAddress();
    Double getLatitude();
    Double getLongitude();
    String getSummary();
    PhotoReference[] getPhotos();
}
