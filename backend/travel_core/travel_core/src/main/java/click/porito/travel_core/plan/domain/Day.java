package click.porito.travel_core.plan.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;

import java.util.List;

@Builder
@JsonTypeName("DAY")
public record Day(
        String dayId,
        List<WayPoint> wayPoints
) implements RouteComponent {
}
