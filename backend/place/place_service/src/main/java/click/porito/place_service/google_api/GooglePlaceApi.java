package click.porito.place_service.google_api;


import click.porito.place_service.dto.FieldMask;
import click.porito.place_service.dto.GooglePlace;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public interface GooglePlaceApi {

    Optional<GooglePlace> placeDetails(String placeId, Set<FieldMask> fields, Locale locale);
    List<GooglePlace> nearbySearch(PlaceNearBySearchQueryDTO option, Set<FieldMask> fields);
    List<GooglePlace> textSearch(PlaceTextSearchQueryDTO option, Set<FieldMask> fields);

}
