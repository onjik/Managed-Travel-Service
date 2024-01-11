package click.porito.travel_core.place.dto;

import java.util.List;

public record PhotoReferenceView(
        String photoId,
        Integer heightPx,
        Integer widthPx,
        List<AuthorAttributionView> authorAttributionViews
) {

}
