package click.porito.travel_core.plan;

import click.porito.travel_core.global.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;

public class PlanNotModifiedException extends PlanBusinessException {
    public PlanNotModifiedException(@NotBlank String planId) {
        super(ErrorCode.RESOURCE_NOT_MODIFIED);
        super.addDetail("planId", planId);
    }

}
