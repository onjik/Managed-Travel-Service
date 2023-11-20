package click.porito.modular_travel.place;

import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PlaceService {

    Optional<GooglePlace> getGooglePlace(String id, DataFetchingFieldSelectionSet selectionSet, @Nullable Locale locale);

    String getPhotoUri(String photoName, int maxWidthPx, int maxHeightPx);

    List<Place> getPlacesNearBy(PlaceInput.PlaceNearBySearchInput input, DataFetchingFieldSelectionSet selectionSet, Locale locale);
    List<Place> getPlacesByTextSearch(PlaceInput.PlaceTextSearchInput input, DataFetchingFieldSelectionSet selectionSet, Locale locale);
}
