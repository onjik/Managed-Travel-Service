package click.porito.account.account.exception;

/**
 * Account 도메인 내에서 발생되고 처리되는 예외의 최상위 클래스
 */
public abstract class AccountBusinessException extends RuntimeException {
    private final static String DOMAIN_NAME = "Account";

    public final String getDomainName() {
        return DOMAIN_NAME;
    }
}
