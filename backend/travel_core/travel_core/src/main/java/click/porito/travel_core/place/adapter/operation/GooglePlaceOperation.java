package click.porito.travel_core.place.adapter.operation;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.PlaceType;
import click.porito.travel_core.place.adapter.operation.google_api.GooglePlaceApi;
import click.porito.travel_core.place.application.operation.PlaceOperation;
import click.porito.travel_core.place.adapter.operation.google_api.GooglePlacePhotoApi;
import click.porito.travel_core.place.adapter.operation.google_api.model.Circle;
import click.porito.travel_core.place.adapter.operation.google_api.model.GooglePlace;
import click.porito.travel_core.place.adapter.operation.google_api.model.PlaceNearByRequestBody;
import click.porito.travel_core.place.adapter.operation.google_api.model.RankPreference;
import click.porito.travel_core.place.domain.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GooglePlaceOperation implements PlaceOperation {
    private final GooglePlaceApi googlePlaceApi;
    private final GooglePlacePhotoApi googlePlacePhotoApi;
    private final Mapper<GooglePlace, Place> googlePlaceMapper;

    @Override
    public Optional<Place> getPlace(String placeId) throws DataAccessException {
        return googlePlaceApi.placeDetails(placeId)
                .map(googlePlaceMapper::map);
    }

    @Override
    public List<Place> getPlaces(String[] placeIds) throws DataAccessException {
        //fetch all places
        ArrayList<GooglePlace> googlePlaces = Arrays.stream(placeIds)
                .map(googlePlaceApi::placeDetails)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));

        return googlePlaceMapper.map(googlePlaces);
    }

    @Override
    public List<Place> getPlaceNearBy(double lat, double lng, int radius, Integer maxResultCount, PlaceType[] placeTypes, Boolean distanceSort) throws DataAccessException {
        Circle circle = Circle.of(lat, lng, radius);
        var builder = PlaceNearByRequestBody.builder();
        builder.locationRestriction(circle);
        if (maxResultCount != null) {
            builder.maxResultCount(maxResultCount);
        }
        if (placeTypes != null) {
            builder.placeTypes(placeTypes);
        }
        if (distanceSort != null && distanceSort) {
            builder.rankPreference(RankPreference.DISTANCE);
        }
        PlaceNearByRequestBody nearByRequestBody = builder.build();
        List<GooglePlace> googlePlaces = googlePlaceApi.nearbySearch(nearByRequestBody);
        return googlePlaceMapper.map(googlePlaces);
    }

    @Override
    public Optional<String> photoUrl(String placeId, String photoId, int maxWidth, int maxHeight) throws DataAccessException {
        String googlePhotoReference = String.format("places/%s/photos/%s", placeId, photoId);
        return googlePlacePhotoApi.photoUri(googlePhotoReference, maxWidth, maxHeight);
    }
}
