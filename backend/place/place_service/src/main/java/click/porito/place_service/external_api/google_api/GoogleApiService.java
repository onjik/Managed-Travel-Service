package click.porito.place_service.external_api.google_api;

import click.porito.place_service.external_api.google_api.exception.GoogleResourceNotFoundException;
import click.porito.place_service.external_api.google_api.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class GoogleApiService implements GooglePlaceApi, GooglePlacePhotoApi {
    private final static String PLACE_DETAIL_URL = "https://places.googleapis.com/v1/places/%s";
    private final static String PLACE_NEARBY_SEARCH_URL = "https://places.googleapis.com/v1/places:searchNearby";
    private final static String PLACE_TEXT_SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    private final static String PLACE_PHOTO_URL = "https://places.googleapis.com/v1/%s/media";
    private final static String GOOGLE_API_HEADER = "X-Goog-Api-Key";
    private final static String PLACE_FIELD_MASK = "X-Goog-FieldMask";
    private final static String LANGUAGE_CODE = "languageCode";
    private final static String FIELD_MASKS_STRING;
    private final static String PREFIXED_FIELD_MASKS_STRING;

    static {
        FIELD_MASKS_STRING = Arrays.stream(FieldMasks.DEFAULT_MASKS)
                .map(FieldMask::getMaskName)
                .reduce((a, b) -> a + "," + b)
                .get();

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
        // Build URI
        String baseUri = String.format(PLACE_DETAIL_URL, placeId);
        
        var builder = new DefaultUriBuilderFactory(baseUri).builder();
        builder.queryParam(LANGUAGE_CODE, Locale.KOREAN.getLanguage());
        var uri = builder.build(placeId);

        // Build Header
        HttpHeaders headers = new HttpHeaders();
        headers.add(GOOGLE_API_HEADER, googleApiContext.getKey());

        headers.add(PLACE_FIELD_MASK, FIELD_MASKS_STRING);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var type = new TypeToken<GooglePlace>() {};
        var response = Optional.ofNullable(
                exchange(uri.toString(), HttpMethod.GET, requestEntity, type).orElse(null)
        );
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
                .orElseThrow();//TODO: 에러처리

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
                .orElseThrow();//TODO: 에러처리

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

            var googleErrorResponse = gson.fromJson(e.getResponseBodyAsString(), GoogleApiException.GoogleErrorResponse.class);
            throw GoogleApiException.of(googleErrorResponse);
        }
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
    public String photoUri(String photoName, int maxWidthPx, int maxHeightPx) {
        String baseUri = String.format(PLACE_PHOTO_URL, photoName);
        var builder = new DefaultUriBuilderFactory(baseUri).builder();
        builder.queryParam("maxHeightPx", maxHeightPx);
        builder.queryParam("maxWidthPx", maxWidthPx);
        builder.queryParam("skipHttpRedirect", true);
        builder.queryParam("key", googleApiContext.getKey());
        URI uri = builder.build();

        final var type = new TypeToken<Map<String, String>>() {};
        Map<String,String> response = exchange(uri.toString(), HttpMethod.GET, null, type)
                .orElseThrow(GoogleResourceNotFoundException::new);
        return response.get("photoUri");
    }
}
