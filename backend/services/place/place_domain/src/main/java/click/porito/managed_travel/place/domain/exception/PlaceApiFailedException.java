package click.porito.managed_travel.place.domain.exception;


import static click.porito.common.exception.ErrorCodes.PLACE_API_OPERATION_FAILED;

/**
 * API 실행중 서버측에서 예외가 발생했음
 */
public class PlaceApiFailedException extends PlaceServerException {
    public PlaceApiFailedException() {
        super(PLACE_API_OPERATION_FAILED);
    }

    public PlaceApiFailedException(Throwable cause) {
        super(cause, PLACE_API_OPERATION_FAILED);
    }
}
