package click.porito.managed_travel.plan.domain.api.reqeust;

import click.porito.managed_travel.plan.domain.api.reqeust.pointer.StructureAwarePointer;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReorderRouteRequest(
        @NotEmpty
        List<StructureAwarePointer> route
) {
}
