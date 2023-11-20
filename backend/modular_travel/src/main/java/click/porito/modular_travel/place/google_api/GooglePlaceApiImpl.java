package click.porito.modular_travel.place.google_api;

import click.porito.modular_travel.place.GooglePlace;
import click.porito.modular_travel.place.config.GoogleApiContext;
import click.porito.modular_travel.place.exception.GoogleResourceNotFoundException;
import click.porito.modular_travel.place.exception.RestClientErrorException;
import click.porito.modular_travel.place.exception.RestServerErrorException;
import click.porito.modular_travel.place.exception.RestUnknownException;
import click.porito.modular_travel.place.model.GooglePlaceModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GooglePlaceApiImpl implements GooglePlaceApi {
    private final static String PLACE_DETAIL_URL = "https://places.googleapis.com/v1/places/%s";
    private final static String PLACE_PHOTO_URL = "https://places.googleapis.com/v1/%s/media";
    private final static String PLACE_NEARBY_SEARCH_URL = "https://places.googleapis.com/v1/places:searchNearby";
    private final static String PLACE_TEXT_SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    private final static String GOOGLE_API_HEADER = "X-Goog-Api-Key";
    private final static String PLACE_FIELD_MASK = "X-Goog-FieldMask";
    private final static String LANGUAGE_CODE = "languageCode";

    private final GoogleApiContext googleApiContext;
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Override
    public Optional<GooglePlace> placeDetails(String placeId, FieldMask[] fields, Locale locale) {
        //URI
        String baseUri = String.format(PLACE_DETAIL_URL, placeId);
        var builder = new DefaultUriBuilderFactory(baseUri).builder();
        if (Objects.nonNull(locale)) {
            builder.queryParam(LANGUAGE_CODE, locale.getLanguage());
        }
        var uri = builder.build(placeId);

        //헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add(GOOGLE_API_HEADER, googleApiContext.apiKey());

        StringBuilder fieldMask = new StringBuilder();

        for (FieldMask field : fields) {
            fieldMask.append(field.getMaskName()).append(",");
        }
        headers.add(PLACE_FIELD_MASK, fieldMask.toString());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var type = new TypeToken<GooglePlaceModel>() {};
        GooglePlaceModel googlePlaceModel = exchange(uri.toString(), HttpMethod.GET, requestEntity, type).orElse(null);
        return Optional.ofNullable(googlePlaceModel);
    }

    @Override
    public String photoUri(String photoReference, int maxWidthPx, int maxHeightPx) {
        String baseUri = String.format(PLACE_PHOTO_URL, photoReference);
        var builder = new DefaultUriBuilderFactory(baseUri).builder();
        builder.queryParam("maxHeightPx", maxHeightPx);
        builder.queryParam("maxWidthPx", maxWidthPx);
        builder.queryParam("skipHttpRedirect", true);
        builder.queryParam("key", googleApiContext.apiKey());
        URI uri = builder.build();

        final var type = new TypeToken<Map<String, String>>() {};
        Map<String,String> response = exchange(uri.toString(), HttpMethod.GET, null, type)
                .orElseThrow(GoogleResourceNotFoundException::new);
        return response.get("photoUri");
    }

    @Override
    public List<GooglePlace> nearbySearch(PlaceNearBySearchOptions option, FieldMask[] fields) {
        StringBuilder masks = new StringBuilder();

        for (FieldMask field : fields) {
            masks.append(field.getPrefixedMaskName()).append(",");
        }
        String googleFieldMasks = masks.toString();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(GOOGLE_API_HEADER, googleApiContext.apiKey());
        headers.add(PLACE_FIELD_MASK, googleFieldMasks);

        //body

        var type = new TypeToken<Map<String,List<GooglePlaceModel>>>() {};
        Map<String,List<GooglePlaceModel>> response = exchange(PLACE_NEARBY_SEARCH_URL, HttpMethod.POST, new HttpEntity<>(option, headers), type)
                .orElseThrow();//TODO: 에러처리
        List<GooglePlaceModel> googlePlaces = response.getOrDefault("places", Collections.emptyList());

        return googlePlaces.stream()
                .map(o -> (GooglePlace) o)
                .toList();
    }

    @Override
    public List<GooglePlace> textSearch(PlaceTextSearchOptions option, FieldMask[] fields) {
        StringBuilder masks = new StringBuilder();
        for (FieldMask field : fields) {
            masks.append(field.getPrefixedMaskName()).append(",");
        }
        String googleFieldMasks = masks.toString();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(GOOGLE_API_HEADER, googleApiContext.apiKey());
        headers.add(PLACE_FIELD_MASK, googleFieldMasks);

        //body

        var type = new TypeToken<Map<String,List<GooglePlaceModel>>>() {};
        Map<String,List<GooglePlaceModel>> response = exchange(PLACE_TEXT_SEARCH_URL, HttpMethod.POST, new HttpEntity<>(option, headers), type)
                .orElseThrow();//TODO: 에러처리

        List<GooglePlaceModel> googlePlaces = response.getOrDefault("places", Collections.emptyList());

        return googlePlaces.stream()
                .map(o -> (GooglePlace) o)
                .toList();
    }

    /**
     * @return responseBody, Optional.empty() if 404 not found
     * @param <T> outputType
     */
    protected <T> Optional<T> exchange(String uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, TypeToken<T> outputType){
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
            return Optional.ofNullable(gson.fromJson(response.getBody(), outputType));
        } catch (HttpClientErrorException.NotFound e) {
            log.debug("Google Api Error : 404 Not Found ( uri : " + uri + " )");
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            log.debug("Google Api Error : 4xx Client Error", e);
            //그 외의 4xx 에러는 모두 잘못된 요청이다.
            throw RestClientErrorException.builder()
                    .uri(uri)
                    .httpMethod(httpMethod)
                    .httpStatus(e.getStatusCode())
                    .message(e.getMessage())
                    .build();
        } catch (HttpServerErrorException e) {
            log.info("Google Api Error : 5xx Google Server Error", e);
            //구글 측 서버 오류
            throw RestServerErrorException.builder()
                    .uri(uri)
                    .httpMethod(httpMethod)
                    .httpStatus(e.getStatusCode())
                    .message(e.getMessage())
                    .build();
        } catch (RestClientException e){
            log.error("Google Api Error : Unknown Error", e);
            //그 외의 예외는 모두 알 수 없는 예외이다.
            throw RestUnknownException.builder()
                    .uri(uri)
                    .httpMethod(httpMethod)
                    .message(e.getMessage())
                    .build();
        } catch (JsonSyntaxException e){
            log.error("Google Api Error : Json Syntax Error", e);
            throw RestUnknownException.builder()
                    .uri(uri)
                    .httpMethod(httpMethod)
                    .message(e.getMessage())
                    .build();
        }
    }

}
