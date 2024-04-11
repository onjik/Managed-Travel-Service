package click.porito.managed_travel.place.domain.request.command;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest extends ReviewCreateRequest{
    private Long reviewId;

    public ReviewUpdateRequest(String placeId, String content, int rating, Long reviewId, List<String> mediaIds) {
        super(placeId, content, rating, mediaIds);
        this.reviewId = reviewId;
    }
}
