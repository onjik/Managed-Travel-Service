package click.porito.travel_core.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @apiNote debugDescription 은 개발자가 디버깅을 위해 사용하는 메시지이다. 프로덕션 환경에서는 노출시키는 것을 권장하지 않는다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0001", "요청한 값이 올바르지 않습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_0002", "해당 리소스를 찾을 수 없습니다."),
    RESOURCE_NOT_MODIFIED(HttpStatus.NOT_MODIFIED, "COMMON_0003", "해당 리소스가 수정되지 않았습니다.(Conditional Request 에 대한 응답)"),

    // Place
    PLACE_DB_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PLACE_0001", "장소 데이터베이스 작업에 실패했습니다."),

    // Plan
    PLAN_OUT_OF_DATE(HttpStatus.BAD_REQUEST, "PLAN_0001", "여행 일정의 버전 정보가 일치하지 않습니다."),
    PLAN_DB_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PLAN_0002", "여행 일정 데이터베이스 작업에 실패했습니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String debugDescription;

    public int getStatusValue() {
        return status.value();
    }

}
