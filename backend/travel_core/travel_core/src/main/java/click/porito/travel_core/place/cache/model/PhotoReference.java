package click.porito.travel_core.place.cache.model;

import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@Data
@TypeAlias("photo_reference")
public final class PhotoReference {

    private String photoId;
    private int heightPx;
    private int widthPx;
    private List<AuthorAttribution> authorAttributions;

    @PersistenceCreator
    public PhotoReference(String photoId, int heightPx, int widthPx, List<AuthorAttribution> authorAttributions) {
        this.photoId = photoId;
        this.heightPx = heightPx;
        this.widthPx = widthPx;
        this.authorAttributions = authorAttributions;
    }

}
