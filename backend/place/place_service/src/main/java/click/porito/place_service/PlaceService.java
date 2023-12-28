package click.porito.place_service;

import click.porito.place_service.model.PlaceDto;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PlaceService {

    /**
     * 장소를 조회합니다.
     *
     * @param placeId 장소 id, not null
     * @return 조회된 장소
     * @throws PlaceRetrieveFailedException 장소 조회에 실패한 경우
     * @throws click.porito.place_service.external_api.ExternalApiException 외부 API 호출에 실패한 경우
     */
    PlaceDto getPlace(String placeId) throws PlaceRetrieveFailedException;

    /**
     * 근처 장소를 조회합니다.
     *
     * @param lat            위도, not null
     * @param lng            경도, not null
     * @param radius         반경, 0 이상 50000 이하
     * @param maxResultCount 최대 조회 결과 수, null 이면 20개
     * @param placeTypes     조회하려는 장소의 타입, null 이면 모든 타입을 조회합니다.
     * @param distanceSort   거리순 정렬 여부, true - 거리순, false - 인기순, null - 인기순
     * @return 조회된 장소 목록
     * @throws click.porito.place_service.external_api.ExternalApiException 외부 API 호출에 실패한 경우
     */
    List<PlaceDto> getNearbyPlaces(double lat, double lng, int radius, @Nullable Integer maxResultCount, @Nullable PlaceType[] placeTypes, @Nullable Boolean distanceSort) ;
}
