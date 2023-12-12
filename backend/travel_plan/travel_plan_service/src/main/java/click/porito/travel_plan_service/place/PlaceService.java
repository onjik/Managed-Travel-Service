package click.porito.travel_plan_service.place;

import click.porito.travel_plan_service.place.dto.Coordinate;
import click.porito.travel_plan_service.place.dto.NearbyQuery;
import click.porito.travel_plan_service.place.dto.PlaceView;

import java.util.List;

public interface PlaceService {
    /**
     * @throws AbstractPlaceFetchingException 장소를 조회하는 중 예외가 발생하였을 때, 유효하지 않은 placeId를 입력한 경우
     * @return placeId에 해당하는 장소 정보, never null
     * @throws AbstractPlaceFetchingException 장소를 조회하는 중 예외가 발생하였을 때
     */
    PlaceView getPlace(String placeId);

    /**
     * 주변의 장소를 조회한다.
     * @param query 쿼리 옵션
     * @return 장소 목록, immutableList, never null
     * @throws jakarta.validation.ConstraintViolationException query의 validation에 실패한 경우
     * @throws AbstractPlaceFetchingException 장소를 조회하는 중 예외가 발생하였을 때
     */
    List<PlaceView> getPlaceNearby(NearbyQuery query);

    /**
     * @param placeId 장소 id
     * @return 장소의 좌표, never null
     * @throws AbstractPlaceFetchingException 장소를 조회하는 중 예외가 발생하였을 때, 유효하지 않은 placeId를 입력한 경우
     */
    Coordinate getCoordinate(String placeId);
}
