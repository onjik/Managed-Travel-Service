package click.porito.place_service.controller;

import click.porito.place_service.dto.Photo;
import click.porito.place_service.dto.Place;
import click.porito.place_service.input.PlaceNearBySearchInput;
import click.porito.place_service.input.PlaceTextSearchInput;
import click.porito.place_service.service.PlaceService;
import graphql.schema.DataFetchingFieldSelectionSet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @QueryMapping(name = "place")
    public Place place(@Argument("id") String id, @Argument("preferredLanguage") Locale preferredLanguage, DataFetchingFieldSelectionSet fieldSelectionSet, Locale acceptLanguage) {
        Locale locale = Optional.ofNullable(preferredLanguage).orElse(acceptLanguage);
        return placeService.getPlace(id, fieldSelectionSet, locale).orElse(null);
    }

    @QueryMapping(name = "imageUri")
    public String imageUri(@Argument("photoName") String photoName, @Argument("maxWidthPx") Integer maxWidthPx, @Argument("maxHeightPx") Integer maxHeightPx) {
        return placeService.getPhotoUri(photoName, maxWidthPx, maxHeightPx);
    }

    @QueryMapping(name ="placesNearBySearch")
    public List<Place> placeNearbySearch(
            @Valid @Argument("input") PlaceNearBySearchInput placeNearBySearchInput,
            @Argument("preferredLanguage") Locale preferredLanguage,
            DataFetchingFieldSelectionSet fieldSelectionSet,
            Locale acceptLanguage) {
        Locale locale = Optional.ofNullable(preferredLanguage).orElse(acceptLanguage);
        return placeService.getPlacesNearBy(placeNearBySearchInput, fieldSelectionSet, locale);
    }

    @QueryMapping(name = "placesTextSearch")
    public List<Place> placesTextSearch(
            @Valid @Argument("input") PlaceTextSearchInput placeNearBySearchInput,
            @Argument("preferredLanguage") Locale preferredLanguage,
            DataFetchingFieldSelectionSet fieldSelectionSet,
            Locale acceptLanguage) {
        Locale locale = Optional.ofNullable(preferredLanguage).orElse(acceptLanguage);
        return placeService.getPlacesByTextSearch(placeNearBySearchInput, fieldSelectionSet, locale);
    }

    @SchemaMapping(typeName = "Photo", field = "photoUri")
    public CompletableFuture<String> photoUri(Photo photo) {
        return CompletableFuture.supplyAsync(() -> {
            return placeService.getPhotoUri(photo.getName(), 1080, 1080);
        });
    }

}
