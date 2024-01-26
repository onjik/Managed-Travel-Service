package click.porito.plan_common.api.reqeust;

import java.time.LocalTime;

public record WayPointDetailUpdateRequest(
        String placeId,
        String memo,
        LocalTime time
) {
}
