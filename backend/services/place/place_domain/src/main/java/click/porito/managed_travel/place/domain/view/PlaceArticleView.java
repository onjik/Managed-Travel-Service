package click.porito.managed_travel.place.domain.view;

import lombok.Data;

import java.time.Instant;

@Data
public class PlaceArticleView {
    private String articleId;
    private String title;
    private String content;
    private String publisherId;
    private String placeId;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isTemp;
}