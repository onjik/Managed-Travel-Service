package click.porito.travel_core.plan.dto;

import java.time.LocalTime;

public record WayPointUpdateForm(
        String wayPointId,
        String placeId,
        String memo,
        LocalTime time
) implements RouteComponentUpdateForm {
    @Override
    public String id() {
        return wayPointId;
    }
}
