package click.porito.managed_travel.plan.domain.exception;


import click.porito.common.exception.ErrorCodes;

public class InvalidRouteReorderRequestException extends PlanBusinessException{
    public InvalidRouteReorderRequestException() {
        super(ErrorCodes.INVALID_PLAN_ROUTE_REORDER_REQUEST);
    }

    public InvalidRouteReorderRequestException(Throwable cause) {
        super(cause, ErrorCodes.INVALID_INPUT_VALUE);
    }
}
