package click.porito.travel_core.place.operation.adapter.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@Data
@TypeAlias("photo_reference")
public final class PhotoEntity {

    private String photoId;
    private int heightPx;
    private int widthPx;
    private List<AuthorAttributionEntity> authorAttributions;

    @PersistenceCreator
    public PhotoEntity(String photoId, int heightPx, int widthPx, List<AuthorAttributionEntity> authorAttributions) {
        this.photoId = photoId;
        this.heightPx = heightPx;
        this.widthPx = widthPx;
        this.authorAttributions = authorAttributions;
    }

}
