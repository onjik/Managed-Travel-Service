package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.LikeStatus;

public interface LikeCommandApi {
    LikeStatus setPlaceArticleLike(Long placeArticleId, LikeStatus likeStatus);
    LikeStatus setPlaceLike(Long placeId, LikeStatus likeStatus);
    LikeStatus setReviewLike(Long reviewId, LikeStatus likeStatus);
    LikeStatus setMediaLike(Long mediaId, LikeStatus likeStatus);
}
