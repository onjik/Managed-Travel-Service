package click.porito.managed_travel.place.domain.request.command;

import lombok.Getter;

@Getter
public class PlaceArticleUpdateRequest extends PlaceArticleCreateRequest {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }
}
