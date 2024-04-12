package click.porito.managed_travel.place.domain.request.command;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlaceArticleUpdateRequest extends PlaceArticleCreateRequest {
    private Long id;
}
