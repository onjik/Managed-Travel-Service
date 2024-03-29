package click.porito.managed_travel.place.domain;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Review {
    private Long reviewId;
    private Integer rating;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private String placeId;
    private String publisherId;
    private List<PlaceMedia> photos;
}