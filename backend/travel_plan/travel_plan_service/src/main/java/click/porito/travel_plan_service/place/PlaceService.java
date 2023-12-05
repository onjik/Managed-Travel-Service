package click.porito.travel_plan_service.place;

import click.porito.travel_plan_service.place.dto.PlaceView;

public interface PlaceService {
    /**
     * @throws AbstractPlaceFetchingException 장소를 조회하는 중 예외가 발생하였을 때, 유효하지 않은 placeId를 입력한 경우
     * @return placeId에 해당하는 장소 정보, never null
     */
    PlaceView getPlace(String placeId);
    PlaceView getPlaceNearby(double latitude, double longitude, int radius);
}
