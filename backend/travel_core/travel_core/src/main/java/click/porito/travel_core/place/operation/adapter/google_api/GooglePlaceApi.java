package click.porito.travel_core.place.operation.adapter.google_api;


import click.porito.travel_core.place.PlaceRetrieveFailedException;
import click.porito.travel_core.place.operation.adapter.google_api.model.GooglePlace;
import click.porito.travel_core.place.operation.adapter.google_api.model.PlaceNearByRequestBody;
import click.porito.travel_core.place.operation.adapter.google_api.model.PlaceTextSearchRequestBody;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.PermissionDeniedDataAccessException;

import java.util.List;
import java.util.Optional;

public interface GooglePlaceApi {

    /**
     * @throws PermissionDeniedDataAccessException API key가 잘못된 경우, 권한이 없는 경우, 호출 횟수 초과
     * @throws InvalidDataAccessResourceUsageException 잘못된 요청
     * @throws PlaceRetrieveFailedException 외부 API 호출에 실패한 경우
     */
    Optional<GooglePlace> placeDetails(String placeId);
    /**
     * @return 조회된 Place들, empty list if not found, never null
     * @throws PermissionDeniedDataAccessException API key가 잘못된 경우, 권한이 없는 경우, 호출 횟수 초과
     * @throws InvalidDataAccessResourceUsageException 잘못된 요청
     * @throws PlaceRetrieveFailedException 외부 API 호출에 실패한 경우
     */
    List<GooglePlace> nearbySearch(PlaceNearByRequestBody option);
    /**
     * @throws PermissionDeniedDataAccessException API key가 잘못된 경우, 권한이 없는 경우, 호출 횟수 초과
     * @throws InvalidDataAccessResourceUsageException 잘못된 요청
     * @throws PlaceRetrieveFailedException 외부 API 호출에 실패한 경우
     */
    List<GooglePlace> textSearch(PlaceTextSearchRequestBody option);

}
