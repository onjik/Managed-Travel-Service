package click.porito.place_service.google_api;

import click.porito.place_service.dto.LocationArea;
import click.porito.place_service.dto.RankPreference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PlaceTextSearchQueryDTO {
    @JsonProperty("textQuery")
    private final String query;
    @JsonProperty("includedType")
    private List<String> includedTypeNames;
    @JsonProperty("languageCode")
    private Locale languageCode;
    @JsonProperty("locationBias")
    private LocationArea locationBias;
    @JsonProperty("locationRestriction")
    private LocationArea locationRestriction;
    @JsonProperty("maxResultCount")
    private Integer maxResultCount;
    @JsonProperty("minRating")
    private Float minRating;
    @JsonProperty("openNow")
    private Boolean openNow;
    @JsonProperty("rankPreference")
    private RankPreference rankPreference;
    @JsonProperty("strictTypeFiltering")
    private Boolean strictTypeFiltering;


}
