package click.porito.plan_common.exception;

import click.porito.common.exception.ErrorCode;
import click.porito.plan_common.api.reqeust.pointer.ComponentPointable;
import org.springframework.lang.Nullable;

public class PointedComponentNotFoundException extends PlanBusinessException {

    public PointedComponentNotFoundException() {
        this(null,null);
    }

    public PointedComponentNotFoundException(@Nullable ComponentPointable<?> pointable) {
        this(pointable,null);
    }

    public PointedComponentNotFoundException(@Nullable ComponentPointable<?> pointable, Throwable cause) {
        super(cause, ErrorCode.POINTED_COMPONENT_NOT_FOUND);
        if (pointable != null) super.addDetail("pointedComponent", pointable);
    }
}
