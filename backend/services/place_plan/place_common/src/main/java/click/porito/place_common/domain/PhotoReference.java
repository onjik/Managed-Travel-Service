package click.porito.place_common.domain;

import java.util.List;

public record PhotoReference(
        String photoId,
        Integer heightPx,
        Integer widthPx,
        List<AuthorAttribution> authorAttributions
) {

}
