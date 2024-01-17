package click.porito.travel_core.plan.api.rest;

import java.time.LocalTime;

public record WayPointUpdateRequest(
        String wayPointId,
        String placeId,
        String memo,
        LocalTime time
) implements RouteComponentRequest {

    @Override
    public String id() {
        return wayPointId;
    }
}
