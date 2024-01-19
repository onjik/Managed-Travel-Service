package click.porito.travel_core.plan;

import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.global.exception.BusinessException;
import click.porito.travel_core.global.exception.ErrorCode;

public class PlanBusinessException extends BusinessException {
    public PlanBusinessException(ErrorCode errorCode) {
        super(Domain.PLAN, errorCode);
    }

    public PlanBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.PLAN, errorCode);
    }
}
