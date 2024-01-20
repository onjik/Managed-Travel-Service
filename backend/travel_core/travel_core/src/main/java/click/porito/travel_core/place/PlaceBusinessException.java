package click.porito.travel_core.place;

import click.porito.travel_core.global.exception.BusinessException;
import click.porito.travel_core.global.exception.ErrorCode;

import static click.porito.travel_core.global.constant.Domain.PLACE;

public class PlaceBusinessException extends BusinessException {
    public PlaceBusinessException(ErrorCode errorCode) {
        super(PLACE, errorCode);
    }

    public PlaceBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, PLACE, errorCode);
    }
}
