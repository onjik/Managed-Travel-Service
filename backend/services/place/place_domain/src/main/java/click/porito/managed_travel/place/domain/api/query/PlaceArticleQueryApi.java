package click.porito.managed_travel.place.domain.api.query;

import click.porito.common.exception.common.AccessDeniedException;
import click.porito.common.exception.common.InvalidInputValueException;
import click.porito.common.exception.common.ResourceNotFoundException;
import click.porito.managed_travel.place.domain.view.PlaceArticleView;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

import java.util.List;

public interface PlaceArticleQueryApi {
    /**
     * 장소 게시글 조회
     * @param id 조회할 장소 게시글 id
     * @return 조회된 장소 게시글
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소 게시글이 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    PlaceArticleView getPlaceArticle(Long id);

    /**
     * 장소 게시글 목록 조회
     * @param placeId 조회할 장소 id
     * @return 해당 장소에 등록된 게시글 id 목록
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    List<Long> getPlaceArticleIdsByPlaceId(Long placeId);

    /**
     * 장소 게시글 검색
     * @param keyword 검색어
     * @return 검색된 장소 게시글 목록
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    List<PlaceArticleView> searchPlaceArticles(String keyword);
}
