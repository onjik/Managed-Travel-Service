package click.porito.place_service.service;

import click.porito.place_service.dto.Place;
import click.porito.place_service.input.PlaceNearBySearchInput;
import click.porito.place_service.input.PlaceTextSearchInput;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PlaceService {

    Optional<Place> getPlace(String id, DataFetchingFieldSelectionSet selectionSet, @Nullable Locale locale);

    String getPhotoUri(String photoName, int maxWidthPx, int maxHeightPx);

    List<Place> getPlacesNearBy(PlaceNearBySearchInput input, DataFetchingFieldSelectionSet selectionSet, Locale locale);
    List<Place> getPlacesByTextSearch(PlaceTextSearchInput input, DataFetchingFieldSelectionSet selectionSet, Locale locale);
}
