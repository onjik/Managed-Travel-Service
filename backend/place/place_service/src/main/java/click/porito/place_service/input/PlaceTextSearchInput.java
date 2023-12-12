package click.porito.place_service.input;

import click.porito.place_service.dto.LocationArea;
import click.porito.place_service.dto.RankPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Optional;

@Setter
@NoArgsConstructor
public
class PlaceTextSearchInput {
    @NotNull
    @NotBlank
    private String query;
    private PlaceTypesInput includeTypes;
    private LocationArea locationBias;
    private LocationArea locationRestriction;
    @Range(min = 1, max = 20, message = "maxResultCount must be between 1 and 20")
    private Integer maxResultCount;
    @Range(min = 0, max = 5, message = "minRating must be between 0 and 5")
    private Float minRating;
    private Boolean openNow;
    private RankPreference rankPreference;
    private Boolean strictTypeFiltering;

    public String getQuery() {
        return query;
    }

    public Optional<PlaceTypesInput> getIncludeTypes() {
        return Optional.ofNullable(includeTypes);
    }

    public Optional<LocationArea> getLocationBias() {
        return Optional.ofNullable(locationBias);
    }

    public Optional<LocationArea> getLocationRestriction() {
        return Optional.ofNullable(locationRestriction);
    }

    public Optional<Integer> getMaxResultCount() {
        return Optional.ofNullable(maxResultCount);
    }

    public Optional<Float> getMinRating() {
        return Optional.ofNullable(minRating);
    }

    public Optional<Boolean> getOpenNow() {
        return Optional.ofNullable(openNow);
    }

    public Optional<RankPreference> getRankPreference() {
        return Optional.ofNullable(rankPreference);
    }

    public Optional<Boolean> getStrictTypeFiltering() {
        return Optional.ofNullable(strictTypeFiltering);
    }
}
