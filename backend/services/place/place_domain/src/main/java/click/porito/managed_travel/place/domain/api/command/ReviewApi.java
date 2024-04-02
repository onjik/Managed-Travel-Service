package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.Review;
import click.porito.managed_travel.place.domain.api.request.ReviewUpsertCommand;
import click.porito.managed_travel.place.domain.exception.PlaceNotFoundException;
import click.porito.managed_travel.place.domain.exception.ReviewNotFoundException;

public interface ReviewApi {

    /**
     * 리뷰를 등록합니다.
     * @param command 리뷰 등록 명령, not null
     * @return 등록된 리뷰
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceNotFoundException 장소가 존재하지 않는 경우
     */
    Review putReview(ReviewUpsertCommand command);

    /**
     * 리뷰를 삭제합니다.
     * @param reviewId 리뷰 id, not null
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws ReviewNotFoundException 리뷰가 존재하지 않는 경우
     */
    void deleteReview(String reviewId);
}
