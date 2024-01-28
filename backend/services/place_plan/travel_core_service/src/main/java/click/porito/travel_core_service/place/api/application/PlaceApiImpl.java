package click.porito.travel_core_service.place.api.application;

import click.porito.place_common.api.PlaceApi;
import click.porito.place_common.api.request.NearBySearchQuery;
import click.porito.place_common.domain.Place;
import click.porito.place_common.domain.PlaceType;
import click.porito.place_common.exception.PlaceRetrieveFailedException;
import click.porito.travel_core_service.place.operation.application.PlaceOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceApiImpl implements PlaceApi {
    private final PlaceOperation placeOperation;

    @PreAuthorize("@placeAccessPolicy.canRead(authentication, #placeId)")
    @Override
    public Optional<Place> getPlace(String placeId) {
        final Optional<Place> place;
        try {
            return placeOperation.getPlace(placeId);
        } catch (DataAccessException e) {
            throw new PlaceRetrieveFailedException(e);
        }
    }

    @PreAuthorize("hasAuthority('place:read:all')")
    @Override
    public List<Place> getPlaces(String[] placeIds) throws PlaceRetrieveFailedException {
        Assert.notNull(placeIds, "placeIds must not be null");
        Assert.noNullElements(placeIds, "placeIds must not contain null");
        try {
            return placeOperation.getPlaces(placeIds);
        } catch (DataAccessException e) {
            throw new PlaceRetrieveFailedException(e);
        }
    }

    @PreAuthorize("hasAuthority('place:read:all')")
    @Valid
    @Override
    public List<Place> getNearbyPlaces(NearBySearchQuery query) throws PlaceRetrieveFailedException {
        Assert.notNull(query, "query must not be null");
        final Double lat = query.latitude();
        final Double lng = query.longitude();
        final Integer maxResultCount = query.maxResultCount();
        final Integer radius = query.radiusMeters();
        final PlaceType[] placeTypes = query.placeTypes();
        final Boolean distanceSort = query.distanceSort();
        // validation
        Assert.isTrue(maxResultCount == null || maxResultCount >= 1 && maxResultCount <= 20, "maxResultCount must be between 1 and 20");
        if (placeTypes != null) {
            Assert.noNullElements(placeTypes, "placeTypes must not contain null");
        }
        try {
            return placeOperation.getPlaceNearBy(lat, lng, radius, maxResultCount, placeTypes, distanceSort);
        } catch (DataAccessException e){
            throw new PlaceRetrieveFailedException(e);
        }

    }

    @PreAuthorize("hasAuthority('place:read:all')")
    @Override
    public Optional<String> getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight) {
        return placeOperation.photoUrl(placeId, photoId, maxWidth, maxHeight);
    }


}
