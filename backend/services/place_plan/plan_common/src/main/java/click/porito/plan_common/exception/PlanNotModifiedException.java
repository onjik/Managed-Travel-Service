package click.porito.plan_common.exception;

import click.porito.common.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;

public class PlanNotModifiedException extends PlanBusinessException {
    public PlanNotModifiedException(@NotBlank String planId) {
        super(ErrorCode.RESOURCE_NOT_MODIFIED);
        super.addDetail("planId", planId);
    }

}
