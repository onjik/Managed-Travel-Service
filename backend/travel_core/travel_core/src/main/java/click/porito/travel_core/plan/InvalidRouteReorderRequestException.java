package click.porito.travel_core.plan;

import click.porito.travel_core.global.exception.ErrorCode;

public class InvalidRouteReorderRequestException extends PlanBusinessException{
    public InvalidRouteReorderRequestException() {
        super(ErrorCode.INVALID_PLAN_ROUTE_REORDER_REQUEST);
    }

    public InvalidRouteReorderRequestException(Throwable cause) {
        super(cause, ErrorCode.INVALID_INPUT_VALUE);
    }
}
