package click.porito.travel_core.plan;

import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.global.exception.ErrorCode;
import click.porito.travel_core.global.exception.ServerException;

public class PlanServerException extends ServerException {
    public PlanServerException(ErrorCode errorCode) {
        super(Domain.PLAN, errorCode);
    }

    public PlanServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.PLAN, errorCode);
    }
}
