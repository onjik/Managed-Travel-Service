package click.porito.travel_core.place;

import click.porito.travel_core.place.dto.PlaceView;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PlaceService {

    /**
     * 장소를 조회합니다.
     *
     * @param placeId 장소 id, not null
     * @return 조회된 장소
     * @throws PlaceRetrieveFailedException 장소 조회에 실패한 경우(없는 게 아니라, 조회 실패)
     */
    Optional<PlaceView> getPlace(String placeId) throws PlaceRetrieveFailedException;


    /**
     * 여러 장소를 한번에 조회합니다.
     * @param placeIds 장소 id 목록, not null
     * @return 조회된 장소 목록, 장소가 존재하지 않을 경우, Empty List
     * @throws PlaceRetrieveFailedException 장소 조회에 실패한 경우(없는 게 아니라, 조회 실패)
     */
    List<PlaceView> getPlaces(String[] placeIds) throws PlaceRetrieveFailedException;

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
     * @throws PlaceRetrieveFailedException 외부 API 호출에 실패한 경우
     */
    List<PlaceView> getNearbyPlaces(@Range(min = -90, max = 90, message = "lat must be between -90 and 90") double lat,
                                    @Range(min = -180, max = 180, message = "lng must be between -180 and 180") double lng,
                                    @Range(min = 0, max = 50000, message = "radiusMeters must be between 0 and 50000") int radius,
                                    @Nullable Integer maxResultCount,
                                    @Nullable PlaceType[] placeTypes,
                                    @Nullable Boolean distanceSort) ;

    Optional<String> getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight);
}
