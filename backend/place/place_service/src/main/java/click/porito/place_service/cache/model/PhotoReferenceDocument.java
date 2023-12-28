package click.porito.place_service.cache.model;

import click.porito.place_service.model.PhotoReference;
import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;

@Data
@TypeAlias("photo_reference")
public final class PhotoReferenceDocument implements PhotoReference {

    private String photoId;
    private String placeId;
    private int heightPx;
    private int widthPx;
    private AuthorAttribution[] authorAttributions;

    @PersistenceCreator
    public PhotoReferenceDocument(String photoId, String placeId, int heightPx, int widthPx, AuthorAttribution[] authorAttributions) {
        this.photoId = photoId;
        this.placeId = placeId;
        this.heightPx = heightPx;
        this.widthPx = widthPx;
        this.authorAttributions = authorAttributions;
    }

    public PhotoReferenceDocument(PhotoReference photoReference) {
        this.photoId = photoReference.getPhotoId();
        this.placeId = photoReference.getPlaceId();
        this.heightPx = photoReference.getHeightPx();
        this.widthPx = photoReference.getWidthPx();
        this.authorAttributions = photoReference.getAuthorAttributions();
    }

    public static PhotoReferenceDocument from(PhotoReference photoReference) {
        return new PhotoReferenceDocument(photoReference);
    }

    @Data
    @TypeAlias("author_attribution")
    static final class AuthorAttributionDocument implements AuthorAttribution {
        private String displayName;
        private String uri;
        private String photoUri;

        public AuthorAttributionDocument(String displayName, String uri, String photoUri) {
            this.displayName = displayName;
            this.uri = uri;
            this.photoUri = photoUri;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String getUri() {
            return uri;
        }

        @Override
        public String getPhotoUri() {
            return photoUri;
        }
    }

}
