package click.porito.place_service.google_api;

import click.porito.place_service.dto.LocationArea;
import click.porito.place_service.dto.RankPreference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Locale;

public class PlaceNearBySearchQueryDTO {

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
    public PlaceNearBySearchQueryDTO(List<String> includedTypeNames, List<String> excludedTypeNames, Locale languageCode, Integer maxResultCount, RankPreference rankPreference, LocationArea locationRestriction) {
        this.includedTypeNames = includedTypeNames;
        this.excludedTypeNames = excludedTypeNames;
        this.languageCode = languageCode;
        this.maxResultCount = maxResultCount;
        this.rankPreference = rankPreference;
        this.locationRestriction = locationRestriction;
    }
}
