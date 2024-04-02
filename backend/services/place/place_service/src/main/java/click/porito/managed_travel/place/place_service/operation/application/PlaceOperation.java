package click.porito.managed_travel.place.place_service.operation.application;

import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.place_service.operation.adapter.google_api.PlaceType;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PlaceOperation {

    boolean exists(String placeId) throws DataAccessException;

    /**
     * id로 장소를 조회한다.
     * @param placeId 조회하려는 장소의 ID, never null
     * @return 조회된 장소, 없으면 empty, never null
     * @throws DataAccessException 데이터 접근에 실패한 경우
     */
    Optional<Place> getPlace(String placeId) throws DataAccessException;

    /**
     * id로 장소들을 조회한다.
     * @param placeIds 조회하려는 장소들의 ID, never null
     * @return 조회된 장소들, 없으면 empty list, never null, 순서를 보장하지 않으며, 없는 장소는 포함하지 않는다.
     * @throws DataAccessException 데이터 접근에 실패한 경우
     */
    List<Place> getPlaces(String[] placeIds) throws DataAccessException;

    /**
     * 주어진 위치에서 가까운 장소들을 조회한다.
     * @param lat 위도, -90 ~ 90
     * @param lng 경도, -180 ~ 180
     * @param radius 반경, 0 ~ 50000
     * @param maxResultCount 최대 결과 개수, null이면 20개
     * @param placeTypes 장소 타입, null이면 전체
     * @param distanceSort 거리순 정렬 여부, null이면 false
     * @return 조회된 장소들, 없으면 empty list, never null
     * @throws DataAccessException 데이터 접근에 실패한 경우
     * <p>
     * <b>주의사항</b> 이 메서드는 위의 사항이 준수되었음을 가정하고 실행됩니다.(validation을 진행하지 않습니다.)
     */
    List<Place> getPlaceNearBy(double lat, double lng, int radius, @Nullable Integer maxResultCount, @Nullable PlaceType[] placeTypes, @Nullable Boolean distanceSort) throws DataAccessException;

    //TODO textSearch


    /**
     * 장소의 사진을 조회한다.
     * @param placeId 장소의 ID, never null
     * @param photoId 사진의 ID, never null
     * @param maxWidth 최대 너비, never null, positive
     * @param maxHeight 최대 높이, never null, positive
     * @return 사진의 URL, never null
     * @throws DataAccessException 데이터 접근에 실패한 경우
     */
    Optional<String> photoUrl(String placeId, String photoId, int maxWidth, int maxHeight) throws DataAccessException;
}
