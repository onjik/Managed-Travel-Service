package click.porito.managed_travel.plan.domain.api.reqeust;

import java.time.LocalTime;

public record WayPointDetailUpdateRequest(
        String placeId,
        String memo,
        LocalTime time
) {
}
