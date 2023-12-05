package click.porito.travel_plan_service.place.dto;

import lombok.Builder;

import java.util.Locale;
import java.util.Map;

@Builder
public record PlaceView(
        String id,
        Map<Locale, String> name,
        Map<Locale, String> description,
        Map<Locale, String> address,
        Coordinate coordinate,
        Float rating,
        Long userRatingCount,
        Map<Locale,String> country,
        Map<Locale,String> city,
        BusinessStatus businessStatus,
        String[] types,
        PlacePhotoReference[] photos
) {
}
