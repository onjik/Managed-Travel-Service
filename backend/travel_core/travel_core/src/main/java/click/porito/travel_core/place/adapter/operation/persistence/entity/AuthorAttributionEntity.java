package click.porito.travel_core.place.adapter.operation.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

@Data
@TypeAlias("author_attribution")
public class AuthorAttributionEntity {
    private String displayName;
    private String uri;
    private String photoUri;

    public AuthorAttributionEntity(String displayName, String uri, String photoUri) {
        this.displayName = displayName;
        this.uri = uri;
        this.photoUri = photoUri;
    }

}
