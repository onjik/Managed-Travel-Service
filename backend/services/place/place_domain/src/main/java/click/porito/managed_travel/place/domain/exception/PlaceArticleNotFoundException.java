package click.porito.managed_travel.place.domain.exception;

public class PlaceArticleNotFoundException extends PlaceResourceNotFoundException {
    public static final String ARTICLE_ID_DETAIL_KEY = "articleId";
    public PlaceArticleNotFoundException(Throwable cause, String articleId) {
        super(cause);
        super.addDetail(ARTICLE_ID_DETAIL_KEY, articleId);
    }
}
