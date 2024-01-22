package click.porito.travel_core.plan.api.reqeust;

import click.porito.travel_core.plan.api.reqeust.pointer.StructureAwarePointer;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReorderRouteRequest(
        @NotEmpty
        List<StructureAwarePointer> route
) {
}
