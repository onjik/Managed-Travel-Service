package click.porito.travel_core_service.place.operation.adapter.google_api;

import click.porito.place_common.exception.PlaceRetrieveFailedException;
import click.porito.travel_core_service.place.operation.adapter.google_api.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlaceApiImpl implements GooglePlaceApi, GooglePlacePhotoApi {
    private final static String PLACE_DETAIL_URL = "https://places.googleapis.com/v1/places/%s";
    private final static String PLACE_NEARBY_SEARCH_URL = "https://places.googleapis.com/v1/places:searchNearby";
    private final static String PLACE_TEXT_SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    private final static String PLACE_PHOTO_URL = "https://places.googleapis.com/v1/%s/media";
    private final static String GOOGLE_API_HEADER = "X-Goog-Api-Key";
    private final static String PLACE_FIELD_MASK = "X-Goog-FieldMask";
    private final static String LANGUAGE_CODE = "languageCode";
    private final static String PREFIXED_FIELD_MASKS_STRING;

    static {
        PREFIXED_FIELD_MASKS_STRING = Arrays.stream(FieldMasks.DEFAULT_MASKS)
                .map(FieldMask::getPrefixedMaskName)
                .reduce((a, b) -> a + "," + b)
                .get();
    }

    private final GoogleApiContext googleApiContext;
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Override
    public Optional<GooglePlace> placeDetails(String placeId) {
        return fetchWithMask(placeId, FieldMasks.DEFAULT_MASKS);
    }

    @Override
    public boolean exists(String placeId) throws PermissionDeniedDataAccessException, InvalidDataAccessResourceUsageException, PlaceRetrieveFailedException {
        Optional<GooglePlace> googlePlace = fetchWithMask(placeId, new FieldMask[]{FieldMask.id});
        return googlePlace.isPresent();
    }

    private Optional<GooglePlace> fetchWithMask(String placeId, FieldMask[] fieldMasks) {
        // Build URI
        String baseUri = String.format(PLACE_DETAIL_URL, placeId);

        var builder = new DefaultUriBuilderFactory(baseUri).builder();
        builder.queryParam(LANGUAGE_CODE, Locale.KOREAN.getLanguage());
        var uri = builder.build(placeId);

        // Build Header
        HttpHeaders headers = new HttpHeaders();
        headers.add(GOOGLE_API_HEADER, googleApiContext.getKey());

        String fieldMaskString = Arrays.stream(fieldMasks)
                .map(FieldMask::getMaskName)
                .reduce((a, b) -> a + "," + b)
                .get();
        headers.add(PLACE_FIELD_MASK, fieldMaskString);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var type = new TypeToken<GooglePlace>() {};
        var response = exchange(uri.toString(), HttpMethod.GET, requestEntity, type);
        response.ifPresent(this::publishEvent);
        return response;
    }


    @Override
    public List<GooglePlace> nearbySearch(PlaceNearByRequestBody option) {
        //header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(GOOGLE_API_HEADER, googleApiContext.getKey());
        headers.add(PLACE_FIELD_MASK, PREFIXED_FIELD_MASKS_STRING);

        //body
        var type = new TypeToken<Map<String,List<GooglePlace>>>() {};
        Map<String,List<GooglePlace>> response = exchange(PLACE_NEARBY_SEARCH_URL, HttpMethod.POST, new HttpEntity<>(option, headers), type)
                .orElse(Collections.emptyMap());

        List<GooglePlace> places = response.getOrDefault("places", Collections.emptyList());
        places.forEach(this::publishEvent);
        return places;
    }

    @Override
    public List<GooglePlace> textSearch(PlaceTextSearchRequestBody option) {
        //header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(GOOGLE_API_HEADER, googleApiContext.getKey());
        headers.add(PLACE_FIELD_MASK, PREFIXED_FIELD_MASKS_STRING);

        //body

        var type = new TypeToken<Map<String,List<GooglePlace>>>() {};
        Map<String,List<GooglePlace>> response = exchange(PLACE_TEXT_SEARCH_URL, HttpMethod.POST, new HttpEntity<>(option, headers), type)
                .orElse(Collections.emptyMap());

        var places = response.getOrDefault("places", Collections.emptyList());
        places.forEach(this::publishEvent);
        return places;
    }


    /**
     * @return responseBody, Optional.empty() if 404 not found
     * @param <T> outputType
     */
    protected <T> Optional<T> exchange(String uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, TypeToken<T> outputType){
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
            return Optional.ofNullable(gson.fromJson(response.getBody(), outputType));
        } catch (RestClientResponseException e) {
            var response = gson.fromJson(e.getResponseBodyAsString(), GoogleErrorResponse.class);
            var code = response.error().code();
            var message = response.error().message();
            if (code == 403) {
                throw new PermissionDeniedDataAccessException("G-API Permission Denied", e);
            } else if (code == 400) {
                if (message.contains("Not a valid PlaceEntity ID")) {
                    return Optional.empty();
                } else {
                    throw new InvalidDataAccessResourceUsageException("Invalid Request", e);
                }
            }
        } catch (Throwable e){
            //여기 까지 오면 에러
            throw new PlaceRetrieveFailedException(e);
        }
        //여기 까지 오면 에러
        throw new PlaceRetrieveFailedException();
    }

    protected void publishEvent(GooglePlace responses) {
//        if (Objects.isNull(responses.getId()) || Objects.isNull(responses.getLocation())) {
//            return;
//        }
//        String placeId = responses.getId();
//        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
//        Double latitude = responses.getLocation().getLatitude();
//        Double longitude = responses.getLocation().getLongitude();
//
//        var event = new GooglePlaceSearchedEvent(placeId, userId, latitude, longitude);
//        try {
//            String json = gson.toJson(event);
//            kafkaTemplate.send("google-place-searched-event", json);
//        } catch (Exception e) {
//            log.error("GooglePlaceSearchedEvent publish error", e);
//            //만약 예외가 발생해도, 응답은 정상적으로 가야하므로
//        }
    }


    @Override
    public Optional<String> photoUri(String photoName, int maxWidthPx, int maxHeightPx) {
        String baseUri = String.format(PLACE_PHOTO_URL, photoName);
        var builder = new DefaultUriBuilderFactory(baseUri).builder();
        builder.queryParam("maxHeightPx", maxHeightPx);
        builder.queryParam("maxWidthPx", maxWidthPx);
        builder.queryParam("skipHttpRedirect", true);
        builder.queryParam("key", googleApiContext.getKey());
        URI uri = builder.build();

        final var type = new TypeToken<Map<String, String>>() {};
        return exchange(uri.toString(), HttpMethod.GET, null, type)
                .map(map -> map.get("photoUri"));
    }
}
