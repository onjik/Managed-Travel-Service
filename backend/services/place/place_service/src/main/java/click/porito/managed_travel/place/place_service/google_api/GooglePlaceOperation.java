package click.porito.managed_travel.place.place_service.google_api;

import click.porito.common.util.Mapper;
import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.place_service.google_api.model.Circle;
import click.porito.managed_travel.place.place_service.google_api.model.GooglePlace;
import click.porito.managed_travel.place.place_service.google_api.model.PlaceNearByRequestBody;
import click.porito.managed_travel.place.place_service.google_api.model.RankPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GooglePlaceOperation implements PlaceOperation {
    private final GooglePlaceApi googlePlaceApi;
    private final GooglePlacePhotoApi googlePlacePhotoApi;
    private final Mapper<GooglePlace, Place> googlePlaceMapper;

    @Override
    public boolean exists(String placeId) throws DataAccessException {
        return googlePlaceApi.exists(placeId);
    }

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
