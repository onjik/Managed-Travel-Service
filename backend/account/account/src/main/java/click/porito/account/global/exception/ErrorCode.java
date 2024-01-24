package click.porito.account.global.exception;

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

    // Account
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT_0001", "해당 계정을 찾을 수 없습니다."),
    ACCOUNT_ALREADY_EXIST(HttpStatus.CONFLICT, "ACCOUNT_0002", "해당 계정은 이미 존재합니다."),
    ACCOUNT_DATABASE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "ACCOUNT_0003", "계정 데이터베이스 예외가 발생했습니다."),

    // Security
    JWT_PROCESSING_SERVER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SECURITY_0001", "JWT 처리에 실패했습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "SECURITY_0002", "JWT 토큰이 만료되었습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "SECURITY_0003", "JWT 토큰이 올바르지 않습니다."),
    JWT_NOT_EXIST(HttpStatus.UNAUTHORIZED, "SECURITY_0004", "JWT 토큰이 존재하지 않습니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String debugDescription;

    public int getStatusValue() {
        return status.value();
    }

}
