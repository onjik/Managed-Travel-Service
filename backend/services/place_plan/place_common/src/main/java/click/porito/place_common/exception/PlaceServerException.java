package click.porito.place_common.exception;

import click.porito.common.exception.ErrorCode;
import click.porito.common.exception.ServerException;

import static click.porito.common.exception.Domain.PLACE;


public class PlaceServerException extends ServerException {
    public PlaceServerException(ErrorCode errorCode) {
        super(PLACE, errorCode);
    }

    public PlaceServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, PLACE, errorCode);
    }
}
