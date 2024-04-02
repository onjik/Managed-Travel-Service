package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.PlaceMedia;
import click.porito.managed_travel.place.domain.api.request.PlaceMediaCreateRequest;
import click.porito.managed_travel.place.domain.exception.PlaceMediaNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

public interface PlaceMediaApi {


    /**
     * PlaceMedia를 생성한다.
     * @param request PlaceMedia 생성 요청, not null
     * @return 생성된 PlaceMedia
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     */
    PlaceMedia createPlaceMedia(PlaceMediaCreateRequest request);

    /**
     * mediaId에 해당하는 PlaceMedia를 삭제한다.
     * @param mediaId mediaId, not null
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceMediaNotFoundException mediaId에 해당하는 PlaceMedia가 존재하지 않는 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     */
    void deleteMedia(String mediaId);
}
