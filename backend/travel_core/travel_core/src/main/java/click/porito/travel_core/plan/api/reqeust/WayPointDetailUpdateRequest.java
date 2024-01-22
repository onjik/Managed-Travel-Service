package click.porito.travel_core.plan.api.reqeust;

import java.time.LocalTime;

public record WayPointDetailUpdateRequest(
        String placeId,
        String memo,
        LocalTime time
) {
}
