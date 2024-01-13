package click.porito.travel_core.plan.domain;

import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@EqualsAndHashCode(of = "wayPointId")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class WayPoint implements RouteComponent {
    private UUID wayPointId;
    private String placeId;
    private String memo;
    private LocalTime time;

    public WayPoint(UUID wayPointId, String placeId) {
        this.wayPointId = wayPointId;
        this.placeId = placeId;
    }
}
