package click.porito.travel_core.plan;

import click.porito.travel_core.global.exception.ErrorCode;

public class PlanOperationFailedException extends PlanServerException{
    public PlanOperationFailedException() {
        super(ErrorCode.PLAN_DB_OPERATION_FAILED);
    }

    public PlanOperationFailedException(Throwable cause) {
        super(cause, ErrorCode.PLAN_DB_OPERATION_FAILED);
    }
}
