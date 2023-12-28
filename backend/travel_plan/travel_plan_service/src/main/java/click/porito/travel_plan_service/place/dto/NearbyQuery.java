package click.porito.travel_plan_service.place.dto;

import lombok.*;

import java.util.Locale;

@Builder
@Setter @Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NearbyQuery {
    private final float latitude;
    private final float longitude;
    private final int radius;
    private Locale locale = Locale.ENGLISH;
    private int limit = 10;
    private RankPreference rankPreference = RankPreference.POPULARITY;

    public enum RankPreference {
        POPULARITY,
        DISTANCE
    }
}
