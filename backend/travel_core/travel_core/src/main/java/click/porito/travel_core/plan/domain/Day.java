package click.porito.travel_core.plan.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@EqualsAndHashCode(of = {"dayId", "wayPoints"})
@Setter @Getter
public non-sealed class Day implements RouteComponent {
    @JsonProperty("dayId")
    private final String dayId;
    @JsonInclude(NON_NULL)
    @JsonProperty("wayPoints")
    private List<WayPoint> wayPoints;

    protected Day(@NonNull String dayId, @NonNull List<WayPoint> wayPoints
    ) {
        this.dayId = dayId;
        this.wayPoints = wayPoints;
    }

    public static Day createEmptyDay() {
        return new Day(UUID.randomUUID().toString(), new ArrayList<>());
    }

    public static Day createDay(List<WayPoint> wayPoints) {
        return new Day(UUID.randomUUID().toString(), wayPoints);
    }


    @Override
    public String toString() {
        return "Day[" +
                "dayId=" + dayId + ", " +
                "wayPoints=" + wayPoints + ']';
    }


}
