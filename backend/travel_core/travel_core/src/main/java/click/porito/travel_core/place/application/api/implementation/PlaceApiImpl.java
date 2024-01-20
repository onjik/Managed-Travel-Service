package click.porito.travel_core.place.application.api.implementation;

import click.porito.travel_core.place.PlaceRetrieveFailedException;
import click.porito.travel_core.place.application.api.PlaceApi;
import click.porito.travel_core.place.PlaceType;
import click.porito.travel_core.place.application.operation.PlaceOperation;
import click.porito.travel_core.place.domain.Place;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PlaceApiImpl implements PlaceApi {
    private final PlaceOperation placeOperation;

    @Override
    public Optional<Place> getPlace(String placeId) {
        final Optional<Place> place;
        try {
            return placeOperation.getPlace(placeId);
        } catch (DataAccessException e) {
            throw new PlaceRetrieveFailedException(e);
        }
    }

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

    @Override
    @Valid
    public List<Place> getNearbyPlaces(@Range(min = -90, max = 90, message = "lat must be between -90 and 90") double lat,
                                       @Range(min = -180, max = 180, message = "lng must be between -180 and 180") double lng,
                                       @Range(min = 0, max = 50000, message = "radiusMeters must be between 0 and 50000") int radius,
                                       @Nullable Integer maxResultCount,
                                       @Nullable PlaceType[] placeTypes,
                                       @Nullable Boolean distanceSort) {
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

    @Override
    public Optional<String> getPhotoUrl(String placeId, String photoId, Integer maxWidth, Integer maxHeight) {
        return placeOperation.photoUrl(placeId, photoId, maxWidth, maxHeight);
    }


}
