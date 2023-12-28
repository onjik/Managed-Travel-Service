package click.porito.place_service.cache;

import click.porito.place_service.model.PlaceDto;

import java.util.List;
import java.util.Optional;

/**
 * 구글에서 조회한 장소 정보를 캐싱하는 서비스
 */
public interface PlaceCacheService {
    Optional<PlaceDto> get(String placeId);
    void put(PlaceDto placeDto);
    void putAll(List<PlaceDto> placeDtos);
}
