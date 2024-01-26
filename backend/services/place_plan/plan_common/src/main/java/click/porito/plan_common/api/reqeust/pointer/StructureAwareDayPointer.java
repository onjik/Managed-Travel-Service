package click.porito.plan_common.api.reqeust.pointer;

import java.util.List;

public record StructureAwareDayPointer(
        String dayId,
        List<StructureAwareWaypointPointer> waypoints
) implements StructureAwarePointer {
}
