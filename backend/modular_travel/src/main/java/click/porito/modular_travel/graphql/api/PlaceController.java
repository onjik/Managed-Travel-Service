package click.porito.modular_travel.graphql.api;

import click.porito.modular_travel.place.Place;
import click.porito.modular_travel.place.PlaceInput;
import click.porito.modular_travel.place.PlaceService;
import graphql.schema.DataFetchingFieldSelectionSet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @QueryMapping(name = "place")
    public Place place(@Argument("id") String id, @Argument("preferredLanguage") Locale preferredLanguage, DataFetchingFieldSelectionSet fieldSelectionSet, Locale acceptLanguage) {
        Locale locale = Optional.ofNullable(preferredLanguage).orElse(acceptLanguage);
        return placeService.getGooglePlace(id, fieldSelectionSet, locale).orElse(null);
    }

    @QueryMapping(name = "imageUri")
    public String imageUri(@Argument("photoName") String photoName, @Argument("maxWidthPx") Integer maxWidthPx, @Argument("maxHeightPx") Integer maxHeightPx) {
        return placeService.getPhotoUri(photoName, maxWidthPx, maxHeightPx);
    }

    @QueryMapping(name ="placesNearBySearch")
    public List<Place> placeNearbySearch(
            @Valid @Argument("input") PlaceInput.PlaceNearBySearchInput placeNearBySearchInput,
            @Argument("preferredLanguage") Locale preferredLanguage,
            DataFetchingFieldSelectionSet fieldSelectionSet,
            Locale acceptLanguage) {
        Locale locale = Optional.ofNullable(preferredLanguage).orElse(acceptLanguage);
        return placeService.getPlacesNearBy(placeNearBySearchInput, fieldSelectionSet, locale);
    }

    @QueryMapping(name = "placesTextSearch")
    public List<Place> placesTextSearch(
            @Valid @Argument("input") PlaceInput.PlaceTextSearchInput placeNearBySearchInput,
            @Argument("preferredLanguage") Locale preferredLanguage,
            DataFetchingFieldSelectionSet fieldSelectionSet,
            Locale acceptLanguage) {
        Locale locale = Optional.ofNullable(preferredLanguage).orElse(acceptLanguage);
        return placeService.getPlacesByTextSearch(placeNearBySearchInput, fieldSelectionSet, locale);
    }

}
