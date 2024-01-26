package click.porito.plan_common.exception;


import click.porito.common.exception.ErrorCode;

public class PlanOperationFailedException extends PlanServerException {
    public PlanOperationFailedException() {
        super(ErrorCode.PLAN_DB_OPERATION_FAILED);
    }

    public PlanOperationFailedException(Throwable cause) {
        super(cause, ErrorCode.PLAN_DB_OPERATION_FAILED);
    }
}
