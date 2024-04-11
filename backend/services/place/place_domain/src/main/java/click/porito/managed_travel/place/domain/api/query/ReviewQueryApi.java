package click.porito.managed_travel.place.domain.api.query;

import click.porito.common.util.PageableRequest;
import click.porito.common.util.SlicedResponse;
import click.porito.managed_travel.place.domain.view.PlaceMediaView;
import click.porito.managed_travel.place.domain.view.ReviewView;

import java.util.Optional;

public interface ReviewQueryApi {

    Optional<ReviewView> getReviewById(Long reviewId);

    SlicedResponse<ReviewView> getReviewsByPlaceId(Long placeId, PageableRequest request);

    SlicedResponse<ReviewView> getReviewsByUserId(Long userId, PageableRequest request);

    Optional<ReviewView> getReviewByPlaceIdAndUserId(Long placeId, Long userId);

    Integer getReviewRatingByPlaceId(Long placeId);


}
