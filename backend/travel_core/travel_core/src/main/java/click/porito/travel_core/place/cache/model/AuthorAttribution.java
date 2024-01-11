package click.porito.travel_core.place.cache.model;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

@Data
@TypeAlias("author_attribution")
public class AuthorAttribution {
    private String displayName;
    private String uri;
    private String photoUri;

    public AuthorAttribution(String displayName, String uri, String photoUri) {
        this.displayName = displayName;
        this.uri = uri;
        this.photoUri = photoUri;
    }

}
