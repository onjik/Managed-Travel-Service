package click.porito.managed_travel.place.domain.api.query;

import click.porito.managed_travel.place.domain.PlaceMedia;
import click.porito.managed_travel.place.domain.exception.PlaceMediaNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

public interface PlaceMediaQuery {
    /**
     * mediaId에 해당하는 PlaceMedia의 URL을 반환한다.
     * @param mediaId mediaId, not null
     * @return mediaId에 해당하는 PlaceMedia의 URL
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceMediaNotFoundException mediaId에 해당하는 PlaceMedia가 존재하지 않는 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     */
    String getPlaceMediaUrl(String mediaId);

    /**
     * photoKey에 해당하는 Google Photo의 URL을 반환한다.
     * @param mediaId mediaId, not null
     * @return photoKey에 해당하는 Google Photo의 URL
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceMediaNotFoundException mediaId에 해당하는 PlaceMedia가 존재하지 않는 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     */
    String getGooglePhotoUrl(String mediaId);

    /**
     * mediaId에 해당하는 PlaceMedia를 반환한다.
     * @param mediaId mediaId, not null
     * @return mediaId에 해당하는 PlaceMedia
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceMediaNotFoundException mediaId에 해당하는 PlaceMedia가 존재하지 않는 경우
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     */
    PlaceMedia getPlaceMedia(String mediaId);
}
