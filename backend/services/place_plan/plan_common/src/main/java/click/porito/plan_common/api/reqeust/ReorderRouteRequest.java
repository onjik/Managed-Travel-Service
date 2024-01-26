package click.porito.plan_common.api.reqeust;

import click.porito.plan_common.api.reqeust.pointer.StructureAwarePointer;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReorderRouteRequest(
        @NotEmpty
        List<StructureAwarePointer> route
) {
}
