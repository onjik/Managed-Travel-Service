package click.porito.managed_travel.place.domain;

import java.util.List;

public record PhotoReference(
        String photoId,
        Integer heightPx,
        Integer widthPx,
        List<AuthorAttribution> authorAttributions
) {

}
