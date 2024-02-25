package click.porito.managed_travel.place.domain.exception;

import click.porito.common.exception.ServerException;
import click.porito.common.exception.common.ErrorCode;

import static click.porito.common.exception.Domain.PLACE;


public class PlaceServerException extends ServerException {
    public PlaceServerException(ErrorCode errorCode) {
        super(PLACE, errorCode);
    }

    public PlaceServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, PLACE, errorCode);
    }
}
