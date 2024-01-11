package click.porito.travel_plan_service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@JsonTypeName("WAYPOINT")
public record WayPointView(
        String waypointId,
        String placeId,
        String memo,
        LocalTime time
) implements RouteComponentView {
}
