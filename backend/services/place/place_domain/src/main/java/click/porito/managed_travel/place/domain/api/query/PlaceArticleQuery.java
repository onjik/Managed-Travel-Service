package click.porito.managed_travel.place.domain.api.query;

import click.porito.common.util.PageableRequest;
import click.porito.common.util.SlicedResponse;
import click.porito.managed_travel.place.domain.PlaceArticle;
import click.porito.managed_travel.place.domain.exception.PlaceArticleNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

public interface PlaceArticleQuery {

    /**
     * 장소의 게시글 목록을 조회합니다.
     * @param placeId 장소 id, not null
     * @param pageableRequest 페이징 요청, not null
     * @return 조회된 게시글 목록
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws PlaceNotFoundException 장소가 존재하지 않는 경우
     */
    SlicedResponse<PlaceArticle> getPlaceArticles(String placeId, PageableRequest pageableRequest);


    /**
     * 게시글을 조회합니다.
     * @param articleId 게시글 id, not null
     * @return 조회된 게시글
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws PlaceArticleNotFoundException 게시글이 존재하지 않는 경우
     */
    PlaceArticle getPlaceArticle(String articleId);


}
