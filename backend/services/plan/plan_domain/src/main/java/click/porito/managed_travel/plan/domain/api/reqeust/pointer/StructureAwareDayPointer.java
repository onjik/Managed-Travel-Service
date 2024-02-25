package click.porito.managed_travel.plan.domain.api.reqeust.pointer;

import java.util.List;

public record StructureAwareDayPointer(
        String dayId,
        List<StructureAwareWaypointPointer> waypoints
) implements StructureAwarePointer {
}
