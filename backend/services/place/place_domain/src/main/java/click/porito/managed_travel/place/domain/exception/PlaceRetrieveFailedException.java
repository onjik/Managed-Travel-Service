package click.porito.managed_travel.place.domain.exception;


import static click.porito.common.exception.ErrorCodes.PLACE_DB_OPERATION_FAILED;

/**
 * 장소 조회 실패 (없는 것이 아니라, 조회 작업이 실패한 서버측 오류를 나타낸다.)
 */
public class PlaceRetrieveFailedException extends PlaceServerException {
    public PlaceRetrieveFailedException() {
        super(PLACE_DB_OPERATION_FAILED);
    }

    public PlaceRetrieveFailedException(Throwable cause) {
        super(cause, PLACE_DB_OPERATION_FAILED);
    }
}
