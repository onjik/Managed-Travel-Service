package click.porito.place_service.external_api.google_api;


import click.porito.place_service.external_api.google_api.model.GooglePlace;
import click.porito.place_service.external_api.google_api.model.PlaceNearByRequestBody;
import click.porito.place_service.external_api.google_api.model.PlaceTextSearchRequestBody;

import java.util.List;
import java.util.Optional;

public interface GooglePlaceApi {

    Optional<GooglePlace> placeDetails(String placeId) throws GoogleApiException;
    List<GooglePlace> nearbySearch(PlaceNearByRequestBody option) throws GoogleApiException;
    List<GooglePlace> textSearch(PlaceTextSearchRequestBody option) throws GoogleApiException;

}
