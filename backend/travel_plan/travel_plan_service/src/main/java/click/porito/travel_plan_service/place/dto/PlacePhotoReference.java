package click.porito.travel_plan_service.place.dto;

import java.util.List;

public class PlacePhotoReference {
    String name;
    int widthPx;
    int heightPx;
    List<AuthorAttribution> authorAttributions;

    public static class AuthorAttribution {
        String displayName;
        String photoUri;
        String uri;
    }


}
