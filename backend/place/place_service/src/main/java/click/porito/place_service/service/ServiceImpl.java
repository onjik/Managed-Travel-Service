package click.porito.place_service.service;

import click.porito.place_service.PhotoService;
import click.porito.place_service.PlaceRetrieveFailedException;
import click.porito.place_service.PlaceService;
import click.porito.place_service.PlaceType;
import click.porito.place_service.cache.PlaceCacheService;
import click.porito.place_service.external_api.google_api.GooglePlaceApi;
import click.porito.place_service.external_api.google_api.GooglePlacePhotoApi;
import click.porito.place_service.external_api.google_api.model.Circle;
import click.porito.place_service.external_api.google_api.model.GooglePlace;
import click.porito.place_service.external_api.google_api.model.PlaceNearByRequestBody;
import click.porito.place_service.external_api.google_api.model.RankPreference;
import click.porito.place_service.model.PlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceImpl implements PlaceService, PhotoService {
    private final GooglePlaceApi googlePlaceApi;
    private final GooglePlacePhotoApi googlePlacePhotoApi;
    private final PlaceCacheService placeCacheService;

    @Override
    public PlaceDto getPlace(String placeId) {
        // check cache
        Optional<PlaceDto> cache = placeCacheService.get(placeId);
        if (cache.isPresent()) {
            log.debug("Cache Hit - Get Place From Cache : {}", placeId);
            return cache.get();
        }
        // cache miss
        // google api
        log.debug("Cache Miss - Get Place From Google API : {}", placeId);

        final GooglePlace place = googlePlaceApi.placeDetails(placeId)
                    .orElseThrow(() -> new PlaceRetrieveFailedException("Place Not Found",null));

        // cache
        log.debug("Cache Put - Put Place To Cache : {}", placeId);
        placeCacheService.put(place);
        return place;
    }

    @Override
    public List<PlaceDto> getNearbyPlaces(double lat, double lng, int radius, @Nullable Integer maxResultCount, @Nullable PlaceType[] placeTypes, @Nullable Boolean distanceSort) {
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
        final List<PlaceDto> results = googlePlaceApi.nearbySearch(builder.build()).stream()
                    .map(o -> (PlaceDto) o)
                    .toList();

        // cache
        placeCacheService.putAll(results);
        return results;
    }

    @Override
    public String getPhotoUrl(String placeId, String photoId, int maxWidth, int maxHeight) {
        String googlePhotoReference = String.format("places/%s/photos/%s", placeId, photoId);
        return googlePlacePhotoApi.photoUri(googlePhotoReference, maxWidth, maxHeight);
    }


}
