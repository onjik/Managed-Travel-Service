package click.porito.modular_travel.account.internal.exception;

import click.porito.modular_travel.global.AbstractBusinessException;

/**
 * Account 도메인 내에서 발생되고 처리되는 예외의 최상위 클래스
 */
public abstract class AccountBusinessException extends AbstractBusinessException {
    private final static String DOMAIN_NAME = "Account";

    @Override
    public final String getDomainName() {
        return DOMAIN_NAME;
    }
}
