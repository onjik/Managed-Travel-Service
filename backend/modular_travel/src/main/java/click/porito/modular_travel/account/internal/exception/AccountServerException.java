package click.porito.modular_travel.account.internal.exception;

import click.porito.modular_travel.global.AbstractUnexpectedServerException;
import click.porito.modular_travel.global.ErrorCode;

/**
 * Account 도메인에서 예상치 못한 서버 에러에 대한 상위 클래스
 */
public final class AccountServerException extends AbstractUnexpectedServerException {

    private final ErrorCode errorCode;

    public AccountServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AccountServerException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static AccountServerException of(ErrorCode errorCode) {
        return new AccountServerException(errorCode);
    }

    public static AccountServerException of(ErrorCode errorCode, String message) {
        return new AccountServerException(errorCode);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
