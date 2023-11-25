package click.porito.modular_travel.account.exception;

/**
 * Account 도메인에서 예상치 못한 서버 에러에 대한 상위 클래스
 */
public final class AccountServerException extends RuntimeException {

    private final AccountErrorCode errorCode;

    public AccountServerException(AccountErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AccountServerException(AccountErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static AccountServerException of(AccountErrorCode errorCode) {
        return new AccountServerException(errorCode);
    }

    public static AccountServerException of(AccountErrorCode errorCode, String message) {
        return new AccountServerException(errorCode);
    }

    public AccountErrorCode getErrorCode() {
        return errorCode;
    }
}
