package click.porito.managed_travel.place.domain.exception;

import click.porito.common.exception.ErrorCodes;
import click.porito.common.exception.common.ErrorCode;

public class PlaceApiFailedException extends PlaceServerException{
    public PlaceApiFailedException() {
        super(ErrorCodes.PLACE_API_OPERATION_FAILED);
    }

    public PlaceApiFailedException(Throwable cause) {
        super(cause, ErrorCodes.PLACE_API_OPERATION_FAILED);
    }
}
