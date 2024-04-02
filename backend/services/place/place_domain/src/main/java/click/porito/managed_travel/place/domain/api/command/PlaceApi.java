package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.api.request.PlaceUpsertCommand;
import click.porito.managed_travel.place.domain.exception.PlaceNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

public interface PlaceApi {

    /**
     * 장소를 등록합니다.
     * @param command 장소 등록 명령, not null
     * @return 등록된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     */
    Place upsertPlace(PlaceUpsertCommand command);

    /**
     * 장소를 삭제합니다.
     * @param placeId placeId, not null
     * @return 삭제 성공 여부
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceNotFoundException 장소가 존재하지 않는 경우
     */
    boolean deletePlace(Long placeId);



}
