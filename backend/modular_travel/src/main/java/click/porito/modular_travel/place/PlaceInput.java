package click.porito.modular_travel.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PlaceInput {
    record ImageUriQueryInput(String photoName, int maxWidthPx, int maxHeightPx) {
    }

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    final class PlaceNearBySearchInput {
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

    /*
        input PlaceTextSearchInput {
            query: String!
            includeTypes: PlaceTypesInput
            locationBias: CircleAreaInput
            locationRestriction: CircleAreaInput
            maxResultCount: NonNegativeInt # 1~20
            minRating: Float # 0~5
            openNow: Boolean
            priceLevel: [PriceLevel!]
            rankPreference: TextSearchRankPreference
            strictTypeFiltering: Boolean
        }

         */
    @Setter
    @NoArgsConstructor
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

    /*
        input PlaceTypesInput {
            automotive: [Automotive!]
            business: [Business!]
            culture: [Culture!]
            education: [Education!]
            entertainmentAndRecreation: [EntertainmentAndRecreation!]
            finance: [Finance!]
            foodAndDrink: [FoodAndDrink!]
            geographicalArea: [GeographicalArea!]
            government: [Government!]
            healthAndWellness: [HealthAndWellness!]
            lodging: [Lodging!]
            placesOfWorship: [PlacesOfWorship!]
            services: [Services!]
            shopping: [Shopping!]
            sports: [Sports!]
            transportation: [Transportation!]
        }
         */
    record PlaceTypesInput (
        List<PlaceType.Automotive> automotive,
        List<PlaceType.Business> business,
        List<PlaceType.Culture> culture,
        List<PlaceType.Education> education,
        List<PlaceType.EntertainmentAndRecreation> entertainmentAndRecreation,
        List<PlaceType.Finance> finance,
        List<PlaceType.FoodAndDrink> foodAndDrink,
        List<PlaceType.GeographicalArea> geographicalArea,
        List<PlaceType.Government> government,
        List<PlaceType.HealthAndWellness> healthAndWellness,
        List<PlaceType.Lodging> lodging,
        List<PlaceType.PlacesOfWorship> placesOfWorship,
        List<PlaceType.Services> services,
        List<PlaceType.Shopping> shopping,
        List<PlaceType.Sports> sports,
        List<PlaceType.Transportation> transportation) {

        public List<String> getAllTypeNames(){
            return Stream.of(
                            this.automotive,
                            this.business,
                            this.culture,
                            this.education,
                            this.entertainmentAndRecreation,
                            this.finance,
                            this.foodAndDrink,
                            this.geographicalArea,
                            this.government,
                            this.healthAndWellness,
                            this.lodging,
                            this.placesOfWorship,
                            this.services,
                            this.shopping,
                            this.sports,
                            this.transportation
                    )
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }

    }

    record PlaceDetailInput(String id, Locale languageCode){
    }
}
