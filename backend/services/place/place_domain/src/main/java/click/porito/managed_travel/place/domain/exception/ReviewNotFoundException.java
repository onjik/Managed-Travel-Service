package click.porito.managed_travel.place.domain.exception;

public class ReviewNotFoundException extends PlaceResourceNotFoundException {
    public static final String REVIEW_ID_DETAIL_KEY = "reviewId";
    public ReviewNotFoundException(Throwable cause, String reviewId) {
        super(cause);
        super.addDetail(REVIEW_ID_DETAIL_KEY, reviewId);
    }
}
