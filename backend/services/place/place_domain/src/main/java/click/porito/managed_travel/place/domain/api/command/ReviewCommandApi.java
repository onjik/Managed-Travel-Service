package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.request.command.ReviewCreateRequest;
import click.porito.managed_travel.place.domain.request.command.ReviewUpdateRequest;
import click.porito.managed_travel.place.domain.view.ReviewView;

public interface ReviewCommandApi {

    ReviewView createReview(ReviewCreateRequest request);

    ReviewView updateReview(ReviewUpdateRequest request);

    void deleteReview(Long reviewId);

}
