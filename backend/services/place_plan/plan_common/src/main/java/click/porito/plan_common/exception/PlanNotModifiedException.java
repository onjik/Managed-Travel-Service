package click.porito.plan_common.exception;

import click.porito.common.exception.ErrorCodes;
import jakarta.validation.constraints.NotBlank;

public class PlanNotModifiedException extends PlanBusinessException {
    public PlanNotModifiedException(@NotBlank String planId) {
        super(ErrorCodes.RESOURCE_NOT_MODIFIED);
        super.addDetail("planId", planId);
    }

}
