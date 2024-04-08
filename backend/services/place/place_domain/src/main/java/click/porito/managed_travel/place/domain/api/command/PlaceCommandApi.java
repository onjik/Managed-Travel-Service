package click.porito.managed_travel.place.domain.api.command;

import click.porito.common.exception.common.AccessDeniedException;
import click.porito.common.exception.common.DataIntegrityViolationException;
import click.porito.common.exception.common.InvalidInputValueException;
import click.porito.common.exception.common.ResourceNotFoundException;
import click.porito.managed_travel.place.domain.request.command.OfficialPlaceCreateRequest;
import click.porito.managed_travel.place.domain.request.command.OfficialPlaceUpdateRequest;
import click.porito.managed_travel.place.domain.request.command.UserPlaceCreateRequest;
import click.porito.managed_travel.place.domain.request.command.UserPlaceUpdateRequest;
import click.porito.managed_travel.place.domain.view.OfficialPlaceView;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;
import click.porito.managed_travel.place.domain.view.UserPlaceView;

public interface PlaceCommandApi {

    /**
     *
     * @param request 등록할 장소 정보
     * @return 등록된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    UserPlaceView createUserPlace(UserPlaceCreateRequest request);

    /**
     * 사용자 장소를 수정합니다.
     * @param request 등록할 장소 정보
     * @return 등록된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    UserPlaceView updateUserPlace(UserPlaceUpdateRequest request);

    /**
     * 공식 장소를 등록합니다.
     * @param request 등록할 장소 정보
     * @return 등록된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    OfficialPlaceView createOfficialPlace(OfficialPlaceCreateRequest request);

    /**
     * 공식 장소를 수정합니다.
     * @param request 수정할 장소 정보
     * @return 수정된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    OfficialPlaceView updateOfficialPlace(OfficialPlaceUpdateRequest request);

    /**
     * 장소를 삭제합니다.
     * @param placeId placeId, not null
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws DataIntegrityViolationException 무결성 제약조건 위반(장소명 중복 등)
     * @throws InvalidInputValueException 잘못된 인자가 주어진 경우
     * @throws ResourceNotFoundException 장소가 존재하지 않는 경우
     * @throws AccessDeniedException 권한이 없는 경우
     */
    void deletePlace(Long placeId);



}
