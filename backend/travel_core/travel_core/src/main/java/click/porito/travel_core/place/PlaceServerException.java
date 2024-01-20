package click.porito.travel_core.place;

import click.porito.travel_core.global.exception.ErrorCode;
import click.porito.travel_core.global.exception.ServerException;

import static click.porito.travel_core.global.constant.Domain.PLACE;

public class PlaceServerException extends ServerException {
    public PlaceServerException(ErrorCode errorCode) {
        super(PLACE, errorCode);
    }

    public PlaceServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, PLACE, errorCode);
    }
}
