package click.porito.travel_core.place.cache;

import click.porito.travel_core.place.dto.PlaceView;

import java.util.List;
import java.util.Optional;

/**
 * 구글에서 조회한 장소 정보를 캐싱하는 서비스
 */
public interface PlaceCacheService {
    Optional<PlaceView> get(String placeId);
    void put(PlaceView placeView);
    void putAll(List<PlaceView> placeViews);
}
