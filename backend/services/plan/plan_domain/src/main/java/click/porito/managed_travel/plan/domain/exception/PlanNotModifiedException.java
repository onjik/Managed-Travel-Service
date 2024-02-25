package click.porito.managed_travel.plan.domain.exception;

import click.porito.common.exception.ErrorCodes;
import jakarta.validation.constraints.NotBlank;

public class PlanNotModifiedException extends PlanBusinessException {
    public PlanNotModifiedException(@NotBlank String planId) {
        super(ErrorCodes.RESOURCE_NOT_MODIFIED);
        super.addDetail("planId", planId);
    }

}
