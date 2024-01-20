package click.porito.travel_core.place.operation.application;

import click.porito.travel_core.place.domain.Place;

import java.util.List;
import java.util.Optional;

/**
 * 구글에서 조회한 장소 정보를 캐싱하는 서비스
 */
public interface PlaceCacheOperation {
    Optional<Place> get(String placeId);

    /**
     * 주어진 placeIds 에 해당하는 장소를 조회합니다.
     * 전부 있지 않은 경우, 있는 것만 반환합니다.
     * @param placeIds 장소 id 목록
     * @return 조회된 장소 목록, 없는 경우, Empty List
     */
    List<Place> getByIdIn(String[] placeIds);
    void put(Place place);
    void putAll(List<Place> places);
}
