package click.porito.managed_travel.plan.domain.exception;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ErrorCode;

public class PlanBusinessException extends BusinessException {
    public PlanBusinessException(ErrorCode errorCode) {
        super(Domain.PLAN, errorCode);
    }

    public PlanBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.PLAN, errorCode);
    }
}
