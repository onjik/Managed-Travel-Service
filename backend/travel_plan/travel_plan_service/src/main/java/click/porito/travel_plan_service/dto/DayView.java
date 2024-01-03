package click.porito.travel_plan_service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("DAY")
public record DayView(
        String dayId,
        WayPointView[] wayPoints
) implements RouteComponentView {
}
