package click.porito.managed_travel.place.porito_travel_spring_boot_starter_place_servlet_connector;

import click.porito.common.exception.Domain;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.AbstractRestConnector;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.RestExchangeable;
import click.porito.managed_travel.place.domain.api.command.PlaceCommandApi;
import click.porito.managed_travel.place.domain.exception.PlaceApiFailedException;
import click.porito.managed_travel.place.domain.request.query.NearBySearchQueryRequest;
import click.porito.managed_travel.place.domain.view.PlaceView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PlaceRestConnector extends AbstractRestConnector implements PlaceCommandApi {

    public PlaceRestConnector(RestExchangeable restExchangeable, String uriPrefix) {
        super(restExchangeable, uriPrefix);
    }

    @Override
    protected Domain getDomain() {
        return Domain.PLACE;
    }

    @Override
    public Optional<PlaceView> getPlace(String placeId) throws PlaceApiFailedException {
        return doExchange("/v1/places/{placeId}", HttpMethod.GET, null, PlaceView.class, placeId);
    }

    @Override
    public boolean exists(String placeId) {
        return true; //TODO 임시임
    }

    @Override
    public List<PlaceView> getPlaces(String[] placeIds) throws PlaceApiFailedException {
        return doExchange(
                "/v1/places?placeIds={placeIds}", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlaceView>>() {
                },
                String.join(",", placeIds)
        ).orElse(Collections.emptyList());
    }

    @Override
    public List<PlaceView> getNearbyPlaces(NearBySearchQueryRequest query) throws PlaceApiFailedException {
        return doExchange(
                "/v1/places/searchNearBy", HttpMethod.POST,
                new HttpEntity<>(query),
                new ParameterizedTypeReference<List<PlaceView>>() {
                }
        ).orElse(Collections.emptyList());
    }

    @Override
    public Optional<String> getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight) {
        //파라미터가 선택이므로 uri 빌더
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/v1/places/{placeId}/photos/{photoId}")
                .queryParam("maxWidthPx", maxWidth)
                .queryParam("maxHeightPx", maxHeight);
        return doExchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                null,
                String.class,
                placeId, photoId
        );

    }
}
