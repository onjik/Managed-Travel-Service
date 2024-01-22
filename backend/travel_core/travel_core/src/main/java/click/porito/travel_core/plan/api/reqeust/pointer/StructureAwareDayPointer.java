package click.porito.travel_core.plan.api.reqeust.pointer;

import java.util.List;

public record StructureAwareDayPointer(
        String dayId,
        List<StructureAwareWaypointPointer> waypoints
) implements StructureAwarePointer {
}
