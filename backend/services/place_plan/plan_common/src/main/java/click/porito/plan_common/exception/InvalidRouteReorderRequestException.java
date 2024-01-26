package click.porito.plan_common.exception;


import click.porito.common.exception.ErrorCode;

public class InvalidRouteReorderRequestException extends PlanBusinessException{
    public InvalidRouteReorderRequestException() {
        super(ErrorCode.INVALID_PLAN_ROUTE_REORDER_REQUEST);
    }

    public InvalidRouteReorderRequestException(Throwable cause) {
        super(cause, ErrorCode.INVALID_INPUT_VALUE);
    }
}
