package click.porito.travel_core.plan.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@EqualsAndHashCode(of = "dayId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Day implements RouteComponent {
    public Day(UUID dayId) {
        this.dayId = dayId;
    }

    private UUID dayId;
    private List<WayPoint> wayPoints = new ArrayList<>();
}
