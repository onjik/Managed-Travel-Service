package click.porito.modular_travel.place.google_api;

import click.porito.modular_travel.place.GooglePlace;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface GooglePlaceApi {

    Optional<GooglePlace> placeDetails(String placeId, FieldMask[] fields, Locale locale);

    String photoUri(String photoReference, int maxWidthPx, int maxHeightPx);
    List<GooglePlace> nearbySearch(PlaceNearBySearchOptions option, FieldMask[] fields);
    List<GooglePlace> textSearch(PlaceTextSearchOptions option, FieldMask[] fields);

}
