package click.porito.place_service.input;

import click.porito.place_service.dto.LocationArea;
import click.porito.place_service.dto.RankPreference;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Optional;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class PlaceNearBySearchInput {
    @NotNull
    private LocationArea locationRestriction;
    @Range(min = 1, max = 20, message = "maxResultCount must be between 1 and 20")
    private Integer maxResultCount = 10;
    private RankPreference rankPreference = RankPreference.POPULARITY;
    private PlaceTypesInput includeTypes;
    private PlaceTypesInput excludeTypes;

    public LocationArea getLocationRestriction() {
        return locationRestriction;
    }

    public Integer getMaxResultCount() {
        return maxResultCount;
    }

    public RankPreference getRankPreference() {
        return rankPreference;
    }

    public Optional<PlaceTypesInput> getIncludeTypes() {
        return Optional.ofNullable(includeTypes);
    }

    public Optional<PlaceTypesInput> getExcludeTypes() {
        return Optional.ofNullable(excludeTypes);
    }
}
