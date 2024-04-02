package click.porito.managed_travel.place.domain.api.query;

import click.porito.common.util.PageableRequest;
import click.porito.common.util.SlicedResponse;
import click.porito.managed_travel.place.domain.Review;
import click.porito.managed_travel.place.domain.exception.PlaceNotFoundException;

import java.util.Optional;

public interface ReviewQuery {
    /**
     * 리뷰를 조회합니다.
     * @param reviewId 리뷰 id, not null
     * @return 조회된 리뷰
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     */
    Optional<Review> getReview(String reviewId);

    /**
     * 장소의 리뷰를 조회합니다.
     * @param placeId 장소 id, not null
     * @param pageableRequest 페이지 요청, not null
     * @return 조회된 리뷰 목록
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceNotFoundException 장소가 존재하지 않는 경우
     */
    SlicedResponse<Review> getPlaceReviews(String placeId, PageableRequest pageableRequest);

}
