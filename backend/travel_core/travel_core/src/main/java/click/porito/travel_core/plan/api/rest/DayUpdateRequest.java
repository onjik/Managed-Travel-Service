package click.porito.travel_core.plan.api.rest;

import java.util.List;

public record DayUpdateRequest(
        String dayId,
        List<WayPointUpdateRequest> wayPoints
) implements RouteComponentRequest {

    @Override
    public String id() {
        return dayId;
    }
}
