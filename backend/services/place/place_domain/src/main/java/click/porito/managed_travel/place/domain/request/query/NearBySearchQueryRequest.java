package click.porito.managed_travel.place.domain.request.query;

import click.porito.managed_travel.place.domain.PlaceCategory;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public record NearBySearchQueryRequest(

        @NotNull
        @Range(min = -90, max = 90, message = "latitude must be between -90 and 90")
        Double latitude,

        @NotNull
        @Range(min = -180, max = 180, message = "longitude must be between -180 and 180")
        Double longitude,

        @NotNull
        @Range(min = 0, max = 50000, message = "radiusMeters must be between 0 and 50000")
        Integer radiusMeters,

        @Range(min = 1, max = 20, message = "maxResultCount must be between 1 and 20")
        Integer maxResultCount,

        PlaceCategory[] placeCategories,

        Boolean distanceSort,

        List<Long> excludePlaceIds
) {
}
