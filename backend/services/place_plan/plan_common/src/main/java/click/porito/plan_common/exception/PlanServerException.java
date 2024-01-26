package click.porito.plan_common.exception;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCode;
import click.porito.common.exception.ServerException;

public class PlanServerException extends ServerException {
    public PlanServerException(ErrorCode errorCode) {
        super(Domain.PLAN, errorCode);
    }

    public PlanServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.PLAN, errorCode);
    }
}
