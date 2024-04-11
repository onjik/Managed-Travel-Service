package click.porito.managed_travel.place.domain.api.query;

import click.porito.managed_travel.place.domain.LikeStatus;

public interface LikeQueryApi {
    LikeStatus getOwnedPlaceArticleLikeStatus(Long placeArticleId);
    int getPlaceArticleLikeCount(Long placeArticleId);
    LikeStatus getOwnedPlaceLikeStatus(Long placeId);
    int getPlaceLikeCount(Long placeId);
    LikeStatus getOwnedReviewLikeStatus(Long reviewId);
    int getReviewLikeCount(Long reviewId);
    LikeStatus getOwnedMediaLikeStatus(Long mediaId);
    int getMediaLikeCount(Long mediaId);
}
