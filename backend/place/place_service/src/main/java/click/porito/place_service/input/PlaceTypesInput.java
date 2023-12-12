package click.porito.place_service.input;

import click.porito.place_service.dto.PlaceType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PlaceTypesInput(
        List<PlaceType> automotive,
        List<PlaceType> business,
        List<PlaceType> culture,
        List<PlaceType> education,
        List<PlaceType> entertainmentAndRecreation,
        List<PlaceType> finance,
        List<PlaceType> foodAndDrink,
        List<PlaceType> geographicalArea,
        List<PlaceType> government,
        List<PlaceType> healthAndWellness,
        List<PlaceType> lodging,
        List<PlaceType> placesOfWorship,
        List<PlaceType> services,
        List<PlaceType> shopping,
        List<PlaceType> sports,
        List<PlaceType> transportation) {

    public List<String> getAllTypeNames() {
        return Stream.of(
                        automotive,
                        business,
                        culture,
                        education,
                        entertainmentAndRecreation,
                        finance,
                        foodAndDrink,
                        geographicalArea,
                        government,
                        healthAndWellness,
                        lodging,
                        placesOfWorship,
                        services,
                        shopping,
                        sports,
                        transportation)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(PlaceType::name)
                .collect(Collectors.toList());
    }

}
