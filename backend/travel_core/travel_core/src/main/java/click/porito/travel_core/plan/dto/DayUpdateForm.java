package click.porito.travel_core.plan.dto;

import java.util.List;

public record DayUpdateForm(
        String dayId,
        List<WayPointUpdateForm> wayPoints
) implements RouteComponentUpdateForm {

    @Override
    public String id() {
        return dayId;
    }
}
