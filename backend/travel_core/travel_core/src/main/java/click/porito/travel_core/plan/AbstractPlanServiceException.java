package click.porito.travel_core.plan;

import org.springframework.core.NestedRuntimeException;

public abstract class AbstractPlanServiceException extends NestedRuntimeException {
    public AbstractPlanServiceException(String msg) {
        super(msg);
    }

    public AbstractPlanServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
