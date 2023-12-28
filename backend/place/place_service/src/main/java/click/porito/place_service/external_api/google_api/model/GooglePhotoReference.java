package click.porito.place_service.external_api.google_api.model;

import click.porito.place_service.model.PhotoReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GooglePhotoReference implements PhotoReference {
    private String name;
    private int heightPx;
    private int widthPx;
    private GoogleAuthorAttribution[] authorAttributions;

    @Override
    public String getPhotoId() {
        return name.split("/")[3];
    }

    @Override
    public String getPlaceId() {
        return name.split("/")[1];
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class GoogleAuthorAttribution implements PhotoReference.AuthorAttribution {
        private String displayName;
        private String uri;
        private String photoUri;
    }
}



