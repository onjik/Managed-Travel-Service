package click.porito.managed_travel.place.domain.exception;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.common.ErrorCode;

import static click.porito.common.exception.Domain.PLACE;

public class PlaceBusinessException extends BusinessException {
    public PlaceBusinessException(ErrorCode errorCode) {
        super(PLACE, errorCode);
    }

    public PlaceBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, PLACE, errorCode);
    }
}
