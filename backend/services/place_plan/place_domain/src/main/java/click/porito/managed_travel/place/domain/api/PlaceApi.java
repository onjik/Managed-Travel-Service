package click.porito.managed_travel.place.domain.api;

import click.porito.managed_travel.place.domain.api.request.NearBySearchQuery;
import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.exception.PlaceRetrieveFailedException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface PlaceApi {

    /**
     * 장소를 조회합니다.
     *
     * @param placeId 장소 id, not null
     * @return 조회된 장소
     * @throws PlaceRetrieveFailedException 장소 조회에 실패한 경우(없는 게 아니라, 조회 실패)
     */
    Optional<Place> getPlace(String placeId) throws PlaceRetrieveFailedException;


    /**
     * 여러 장소를 한번에 조회합니다.
     * @param placeIds 장소 id 목록, not null
     * @return 조회된 장소 목록, 장소가 존재하지 않을 경우, Empty List
     * @throws PlaceRetrieveFailedException 장소 조회에 실패한 경우(없는 게 아니라, 조회 실패)
     */
    List<Place> getPlaces(String[] placeIds) throws PlaceRetrieveFailedException;

    /**
     * 근처 장소를 조회합니다.
     *
     * @param query 근처 장소 조회 쿼리, not null
     * @return 조회된 장소 목록
     * @throws PlaceRetrieveFailedException 외부 API 호출에 실패한 경우
     */
    List<Place> getNearbyPlaces(@Valid NearBySearchQuery query) throws PlaceRetrieveFailedException;

    Optional<String> getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight);
}
