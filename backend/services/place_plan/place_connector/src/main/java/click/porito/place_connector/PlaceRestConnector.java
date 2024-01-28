package click.porito.place_connector;

import click.porito.common.exception.Domain;
import click.porito.connector.AbstractRestConnector;
import click.porito.place_common.api.PlaceApi;
import click.porito.place_common.api.request.NearBySearchQuery;
import click.porito.place_common.domain.Place;
import click.porito.place_common.exception.PlaceRetrieveFailedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PlaceRestConnector extends AbstractRestConnector implements PlaceApi {
    public PlaceRestConnector(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected Domain getDomain() {
        return Domain.PLACE;
    }

    @Override
    public Optional<Place> getPlace(String placeId) throws PlaceRetrieveFailedException {
        return doExchange(
                restTemplate -> restTemplate.getForEntity("/v1/places/{placeId}", Place.class, placeId)
        );
    }

    @Override
    public List<Place> getPlaces(String[] placeIds) throws PlaceRetrieveFailedException {
        return doExchange(
                restTemplate -> restTemplate.exchange(
                        "/v1/places?placeIds={placeIds}", HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Place>>() {
                        },
                        String.join(",", placeIds)
                )
        ).orElse(Collections.emptyList());
    }

    @Override
    public List<Place> getNearbyPlaces(NearBySearchQuery query) throws PlaceRetrieveFailedException {
        return doExchange(
                restTemplate -> restTemplate.exchange(
                        "/v1/places/searchNearBy", HttpMethod.POST,
                        new HttpEntity<>(query),
                        new ParameterizedTypeReference<List<Place>>() {}
                )
        ).orElse(Collections.emptyList());
    }

    @Override
    public Optional<String> getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight) {
        //파라미터가 선택이므로 uri 빌더
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/v1/places/{placeId}/photos/{photoId}")
                .queryParam("maxWidthPx", maxWidth)
                .queryParam("maxHeightPx", maxHeight);
        return doExchange(
                restTemplate -> restTemplate.getForEntity(
                        uriComponentsBuilder.toUriString(),
                        String.class,
                        placeId, photoId
                )
        );

    }
}
