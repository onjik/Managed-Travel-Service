package click.porito.travel_core.plan.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@EqualsAndHashCode(of = "dayId")
public final class Day implements RouteComponent {
    private UUID dayId;
    private List<WayPoint> wayPoints;
}
