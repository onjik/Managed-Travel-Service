package click.porito.managed_travel.place.domain.api.command;

import click.porito.common.exception.common.AccessDeniedException;
import click.porito.common.exception.common.DataIntegrityViolationException;
import click.porito.common.exception.common.InvalidInputValueException;
import click.porito.common.exception.common.ResourceNotFoundException;
import click.porito.managed_travel.place.domain.view.PlaceArticleView;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;
import click.porito.managed_travel.place.domain.request.command.PlaceArticleCreateRequest;
import click.porito.managed_travel.place.domain.request.command.PlaceArticleUpdateRequest;

public interface PlaceArticleCommandApi {
    /**
     * PlaceArticleView 생성
     * @param request 장소 게시글 생성 요청
     * @return 생성된 장소 게시글
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */

    PlaceArticleView createPlaceArticle(PlaceArticleCreateRequest request);

    /**
     * PlaceArticleView 수정
     * @param request 수정할 장소 게시글 정보
     * @return 수정된 장소 게시글
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    PlaceArticleView updatePlaceArticle(PlaceArticleUpdateRequest request);

    /**
     * PlaceArticleView 삭제
     * @param id 삭제할 장소 게시글 id
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    void deletePlaceArticle(Long id);

}
