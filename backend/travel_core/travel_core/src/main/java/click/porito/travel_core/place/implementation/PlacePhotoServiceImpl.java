package click.porito.travel_core.place.implementation;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.PhotoService;
import click.porito.travel_core.place.PlaceRetrieveFailedException;
import click.porito.travel_core.place.PlaceService;
import click.porito.travel_core.place.PlaceType;
import click.porito.travel_core.place.cache.PlaceCacheService;
import click.porito.travel_core.place.dao.GooglePlacePhotoRepository;
import click.porito.travel_core.place.dao.GooglePlaceRepository;
import click.porito.travel_core.place.dao.google_api.model.Circle;
import click.porito.travel_core.place.dao.google_api.model.GooglePlace;
import click.porito.travel_core.place.dao.google_api.model.PlaceNearByRequestBody;
import click.porito.travel_core.place.dao.google_api.model.RankPreference;
import click.porito.travel_core.place.dto.PlaceView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlacePhotoServiceImpl implements PlaceService, PhotoService {
    private final GooglePlaceRepository googlePlaceRepository;
    private final GooglePlacePhotoRepository googlePlacePhotoRepository;

    private final Mapper<GooglePlace, PlaceView> googlePlaceViewMapper;

    private final PlaceCacheService placeCacheService;

    @Override
    public Optional<PlaceView> getPlace(String placeId) {
        // check cache
        Optional<PlaceView> cache = placeCacheService.get(placeId);
        if (cache.isPresent()) {
            log.debug("Cache Hit - Get Place From Cache : {}", placeId);
            return cache;
        }
        // cache miss
        // google api
        log.debug("Cache Miss - Get Place From Google API : {}", placeId);

        final Optional<PlaceView> place;
        try {
            place = googlePlaceRepository.placeDetails(placeId)
                    .map(googlePlaceViewMapper::map);
        } catch (DataAccessException e) {
            throw new PlaceRetrieveFailedException("Place Not Found", e);
        }
        if (place.isPresent()){
            // cache
            log.debug("Cache Put - Put Place To Cache : {}", placeId);
            placeCacheService.put(place.get());
        }
        return place;
    }

    @Override
    public List<PlaceView> getNearbyPlaces(double lat, double lng, int radius, @Nullable Integer maxResultCount, @Nullable PlaceType[] placeTypes, @Nullable Boolean distanceSort) {
        // validation
        Assert.isTrue(lat >= -90 && lat <= 90, "lat must be between -90 and 90");
        Assert.isTrue(lng >= -180 && lng <= 180, "lng must be between -180 and 180");
        Assert.isTrue(radius >= 0 && radius <= 50000, "radiusMeters must be between 0 and 50000");
        Assert.isTrue(maxResultCount == null || maxResultCount >= 1 && maxResultCount <= 20, "maxResultCount must be between 1 and 20");
        if (placeTypes != null) {
            Assert.noNullElements(placeTypes, "placeTypes must not contain null");
        }


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

        // google api
        final List<PlaceView> results;
        try {
            results = googlePlaceViewMapper.map(
                    googlePlaceRepository.nearbySearch(builder.build())
            );
        } catch (DataAccessException e){
            throw new PlaceRetrieveFailedException("Place Not Found", e);
        }

        // cache
        placeCacheService.putAll(results);
        return results;
    }

    @Override
    public String getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight) {
        String googlePhotoReference = String.format("places/%s/photos/%s", placeId, photoId);
        return googlePlacePhotoRepository.photoUri(googlePhotoReference, maxWidth, maxHeight);
    }


}
