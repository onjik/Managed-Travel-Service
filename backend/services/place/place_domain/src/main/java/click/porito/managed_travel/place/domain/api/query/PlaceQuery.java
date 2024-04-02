package click.porito.managed_travel.place.domain.api.query;

import click.porito.common.util.PageableRequest;
import click.porito.common.util.SlicedResponse;
import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.PlaceMedia;
import click.porito.managed_travel.place.domain.api.request.NearBySearchQuery;
import click.porito.managed_travel.place.domain.exception.PlaceApiFailedException;
import click.porito.managed_travel.place.domain.exception.PlaceNotFoundException;
import click.porito.managed_travel.place.domain.exception.PlaceServerException;

import java.util.List;
import java.util.Optional;

public interface PlaceQuery {

    /**
     * 장소를 조회합니다.
     *
     * @param placeId 장소 id, not null
     * @return 조회된 장소
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     */
    Optional<Place> getPlaceById(Long placeId);

    /**
     * placeId로 장소가 존재하는지 확인합니다.
     * @param placeId 장소 id, not null
     * @return 장소가 존재하는지 여부
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     */
    boolean existsByPlaceId(Long placeId);


    /**
     * 여러 장소를 한번에 조회합니다.
     * @param placeIds 장소 id 목록, not null
     * @return 조회된 장소 목록, 장소가 존재하지 않을 경우, Empty List
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     */
    List<Place> getPlaces(Long[] placeIds);

    /**
     * 근처 장소를 조회합니다.
     * @param query 근처 장소 조회 쿼리, not null
     * @return 조회된 장소 목록
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     */
    List<Place> getNearbyPlaces(NearBySearchQuery query);

    /**
     * 장소의 사진을 조회합니다.
     * @param placeId 장소 id, not null
     * @return 장소 사진 목록
     * @throws PlaceServerException 장소 API 호출 중 서버측 에러 발생
     * @throws IllegalArgumentException 잘못된 인자가 주어진 경우
     * @throws PlaceNotFoundException 장소가 존재하지 않는 경우
     */
    SlicedResponse<PlaceMedia> getPlacePhotoReference(Long placeId, PageableRequest request) throws PlaceApiFailedException;
}
