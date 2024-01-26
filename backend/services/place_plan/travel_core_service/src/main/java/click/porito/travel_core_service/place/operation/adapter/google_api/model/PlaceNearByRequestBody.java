package click.porito.travel_core_service.place.operation.adapter.google_api.model;

import click.porito.place_common.domain.PlaceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PlaceNearByRequestBody {

    @JsonProperty("includedTypes")
    private List<String> includedTypeNames;

    @JsonProperty("excludedTypes")
    private List<String> excludedTypeNames;
    @JsonProperty("languageCode")
    private Locale languageCode;
    @JsonProperty("maxResultCount")
    private Integer maxResultCount;
    @JsonProperty("rankPreference")
    private RankPreference rankPreference;
    @JsonProperty("locationRestriction")
    private LocationArea locationRestriction;

    @Builder
    public PlaceNearByRequestBody(PlaceType[] placeTypes, Locale languageCode, Integer maxResultCount, RankPreference rankPreference, Circle locationRestriction) {
        // 반경은 0.0 이상 50000.0 이하
        Assert.notNull(locationRestriction, "locationRestriction must not be null");
        if (placeTypes != null) {
            Assert.noNullElements(placeTypes, "placeTypes must not contain null");
        }
        double radiusMeters = locationRestriction.getRadius();
        if (radiusMeters < 0.0 || radiusMeters > 50000.0){
            throw new IllegalArgumentException("radiusMeters must be between 0.0 and 50000.0");
        }

        if (placeTypes != null) {
            this.includedTypeNames = Arrays.stream(placeTypes)
                    .map(PlaceType::getTypeNames)
                    .flatMap(Arrays::stream)
                    .toList();
        }
        this.languageCode = languageCode;
        this.maxResultCount = maxResultCount;
        this.rankPreference = rankPreference;
        this.locationRestriction = new LocationArea(locationRestriction);
    }
}
