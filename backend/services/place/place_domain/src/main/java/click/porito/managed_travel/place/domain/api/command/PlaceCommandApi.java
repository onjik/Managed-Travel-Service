package click.porito.managed_travel.place.domain.api.command;

import click.porito.common.exception.common.AccessDeniedException;
import click.porito.common.exception.common.DataIntegrityViolationException;
import click.porito.common.exception.common.ResourceNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;
import click.porito.managed_travel.place.domain.request.command.PlaceUpsertRequest;
import click.porito.managed_travel.place.domain.view.PlaceView;

public interface PlaceCommandApi {

    /**
     * 장소를 등록하거나 수정합니다.
     * @param request 등록할 장소 정보
     * @return 등록된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    PlaceView upsertPlace(PlaceUpsertRequest request);

    /**
     * 장소를 삭제합니다.
     * @param placeId placeId, not null
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    void deletePlace(Long placeId);

}
