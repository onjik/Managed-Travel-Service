package click.porito.travel_plan_service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@EqualsAndHashCode(of = "wayPointId")
@Getter @Setter
public final class WayPoint implements RouteComponent {
    private UUID wayPointId;
    private String placeId;
    private String memo;
    private LocalTime time;
}
