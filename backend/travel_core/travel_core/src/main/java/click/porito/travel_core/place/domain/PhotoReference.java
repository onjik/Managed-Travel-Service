package click.porito.travel_core.place.domain;

import java.util.List;

public record PhotoReference(
        String photoId,
        Integer heightPx,
        Integer widthPx,
        List<AuthorAttribution> authorAttributions
) {

}
