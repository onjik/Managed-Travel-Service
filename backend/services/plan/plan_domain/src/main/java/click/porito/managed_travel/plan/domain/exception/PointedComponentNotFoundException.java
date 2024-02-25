package click.porito.managed_travel.plan.domain.exception;

import click.porito.common.exception.ErrorCodes;
import click.porito.managed_travel.plan.domain.api.reqeust.pointer.ComponentPointable;
import org.springframework.lang.Nullable;

public class PointedComponentNotFoundException extends PlanBusinessException {

    public PointedComponentNotFoundException() {
        this(null,null);
    }

    public PointedComponentNotFoundException(@Nullable ComponentPointable<?> pointable) {
        this(pointable,null);
    }

    public PointedComponentNotFoundException(@Nullable ComponentPointable<?> pointable, Throwable cause) {
        super(cause, ErrorCodes.POINTED_COMPONENT_NOT_FOUND);
        if (pointable != null) super.addDetail("pointedComponent", pointable);
    }
}
