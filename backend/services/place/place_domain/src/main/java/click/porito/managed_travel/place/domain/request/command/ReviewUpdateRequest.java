package click.porito.managed_travel.place.domain.request.command;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest extends ReviewCreateRequest{
    private Long reviewId;

    public ReviewUpdateRequest(String placeId, String content, int rating, Long reviewId) {
        super(placeId, content, rating);
        this.reviewId = reviewId;
    }
}
