package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.PlaceArticle;
import click.porito.managed_travel.place.domain.api.request.PlaceArticleUpsertCommand;
import click.porito.managed_travel.place.domain.exception.PlaceArticleNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

public interface PlaceArticleApi {


    /**
     * 게시글을 등록 또는 수정합니다.
     * @param command 게시글 등록 또는 수정 명령, not null
     * @return 등록 또는 수정된 게시글
     */
    PlaceArticle putPlaceArticle(PlaceArticleUpsertCommand command);

    /**
     * 게시글을 삭제합니다.
     * @param articleId 게시글 id, not null
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws PlaceArticleNotFoundException 게시글이 존재하지 않는 경우
     */
    void deletePlaceArticle(String articleId);
}
