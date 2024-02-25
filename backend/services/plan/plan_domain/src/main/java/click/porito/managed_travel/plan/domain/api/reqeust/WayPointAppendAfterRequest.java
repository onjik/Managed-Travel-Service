package click.porito.managed_travel.plan.domain.api.reqeust;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record WayPointAppendAfterRequest(
        @NotBlank
        String placeId,
        @Nullable
        String dayId,
        @Nullable
        String wayPointId
) {
}
