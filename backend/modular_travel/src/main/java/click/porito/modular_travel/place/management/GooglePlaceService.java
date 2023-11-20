package click.porito.modular_travel.place.management;

import click.porito.modular_travel.place.*;
import click.porito.modular_travel.place.google_api.FieldMask;
import click.porito.modular_travel.place.google_api.GooglePlaceApi;
import click.porito.modular_travel.place.google_api.PlaceNearBySearchOptions;
import click.porito.modular_travel.place.google_api.PlaceTextSearchOptions;
import click.porito.modular_travel.place.model.LocationArea;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlaceService implements PlaceService {

    private final GooglePlaceApi googlePlaceApi;

    @Override
    public final Optional<GooglePlace> getGooglePlace(String id, DataFetchingFieldSelectionSet selectionSet, @Nullable Locale locale) {
        FieldMask[] fieldMasks = convertToMaskFields(selectionSet).toArray(FieldMask[]::new);
        return googlePlaceApi.placeDetails(id, fieldMasks, locale);
    }

    @Override
    public String getPhotoUri(String photoName, int maxWidthPx, int maxHeightPx) {
        return googlePlaceApi.photoUri(photoName, maxWidthPx, maxHeightPx);
    }

    @Override
    public List<Place> getPlacesNearBy(PlaceInput.PlaceNearBySearchInput input, DataFetchingFieldSelectionSet selectionSet, Locale languageCode){
        List<String> includeTypeNames = input.getIncludeTypes().map(PlaceInput.PlaceTypesInput::getAllTypeNames).orElse(Collections.emptyList());
        List<String> excludeTypeNames = input.getExcludeTypes().map(PlaceInput.PlaceTypesInput::getAllTypeNames).orElse(Collections.emptyList());
        Integer maxResultCount = input.getMaxResultCount();
        RankPreference rankPreference = input.getRankPreference();
        LocationArea locationRestriction = input.getLocationRestriction();

        FieldMask[] fieldMasks = convertToMaskFields(selectionSet).toArray(FieldMask[]::new);
        PlaceNearBySearchOptions requestBody = PlaceNearBySearchOptions.builder()
                .includedTypeNames(includeTypeNames)
                .excludedTypeNames(excludeTypeNames)
                .languageCode(languageCode)
                .maxResultCount(maxResultCount)
                .rankPreference(rankPreference)
                .locationRestriction(locationRestriction)
                .build();

        return googlePlaceApi.nearbySearch(requestBody, fieldMasks).stream().map(o -> (Place) o).toList();
    }

    @Override
    public List<Place> getPlacesByTextSearch(PlaceInput.PlaceTextSearchInput input, DataFetchingFieldSelectionSet selectionSet, Locale locale) {
        //body
        PlaceTextSearchOptions requestBody = new PlaceTextSearchOptions(input.getQuery());

        input.getIncludeTypes().ifPresent(placeTypesInput -> requestBody.setIncludedTypeNames(placeTypesInput.getAllTypeNames()));
        Optional.ofNullable(locale).ifPresent(requestBody::setLanguageCode);
        input.getLocationBias().ifPresent(requestBody::setLocationBias);
        input.getLocationRestriction().ifPresent(requestBody::setLocationRestriction);
        input.getMaxResultCount().ifPresent(requestBody::setMaxResultCount);
        input.getMinRating().ifPresent(requestBody::setMinRating);
        input.getOpenNow().ifPresent(requestBody::setOpenNow);
        input.getRankPreference().ifPresent(requestBody::setRankPreference);
        input.getStrictTypeFiltering().ifPresent(requestBody::setStrictTypeFiltering);

        FieldMask[] fieldMask = convertToMaskFields(selectionSet).toArray(FieldMask[]::new);
        return googlePlaceApi.textSearch(requestBody, fieldMask)
                .stream()
                .map(o -> (Place) o)
                .toList();
    }

    protected Set<FieldMask> convertToMaskFields(DataFetchingFieldSelectionSet selectionSet) {
        return selectionSet.getImmediateFields().stream()
                .map(SelectedField::getQualifiedName)
                .map(SchemaSelectedFieldsMapper::valueOfIfExists)
                .filter(Objects::nonNull)
                .map(SchemaSelectedFieldsMapper::getFieldMask)
                .collect(Collectors.toSet());
    }


    @Getter
    protected enum SchemaSelectedFieldsMapper {
        id(FieldMask.id),
        businessStatus(FieldMask.businessStatus),
        displayName(FieldMask.displayName),
        location(FieldMask.location),
        formattedAddress(FieldMask.formattedAddress),
        types(FieldMask.types),
        utcOffsetMinutes(FieldMask.utcOffsetMinutes),
        photos(FieldMask.photos),
        primaryType(FieldMask.primaryType),
        primaryTypeDisplayName(FieldMask.primaryTypeDisplayName),
        shortFormattedAddress(FieldMask.shortFormattedAddress),
        iconBackgroundUri(FieldMask.iconBackgroundUri),
        iconMaskBaseUri(FieldMask.iconMaskBaseUri),
        googleMapsUri(FieldMask.googleMapsUri)
        ;
        private final FieldMask fieldMask;

        public static SchemaSelectedFieldsMapper valueOfIfExists(String name) {
            try {
                return valueOf(name);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        SchemaSelectedFieldsMapper(FieldMask fieldMask) {
            this.fieldMask = fieldMask;
        }
    }


}
