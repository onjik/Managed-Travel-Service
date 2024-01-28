package click.porito.plan_common.exception;


import click.porito.common.exception.ErrorCodes;

public class PlanOperationFailedException extends PlanServerException {
    public PlanOperationFailedException() {
        super(ErrorCodes.PLAN_DB_OPERATION_FAILED);
    }

    public PlanOperationFailedException(Throwable cause) {
        super(cause, ErrorCodes.PLAN_DB_OPERATION_FAILED);
    }
}
