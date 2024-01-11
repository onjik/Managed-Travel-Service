package click.porito.travel_core.plan.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;

import java.util.List;

@Builder
@JsonTypeName("DAY")
public record DayView(
        String dayId,
        List<WayPointView> wayPoints
) implements RouteComponentView {
}
