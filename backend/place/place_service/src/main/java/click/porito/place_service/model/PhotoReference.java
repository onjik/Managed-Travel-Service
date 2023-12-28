package click.porito.place_service.model;

public interface PhotoReference {
    String getPhotoId();
    String getPlaceId();
    int getHeightPx();
    int getWidthPx();
    AuthorAttribution[] getAuthorAttributions();


    interface AuthorAttribution {
        String getDisplayName();
        String getUri();
        String getPhotoUri();
    }

}
