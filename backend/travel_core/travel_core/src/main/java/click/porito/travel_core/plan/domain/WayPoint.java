package click.porito.travel_core.plan.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@JsonTypeName("WAYPOINT")
public record WayPoint(
        String waypointId,
        String placeId,
        String memo,
        LocalTime time
) implements RouteComponent {
}
