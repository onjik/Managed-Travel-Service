package click.porito.account.account.exception;

import click.porito.account.global.constant.Domain;
import click.porito.account.global.exception.BusinessException;
import click.porito.account.global.exception.ErrorCode;

/**
 * AccountEntity 도메인 내에서 발생되고 처리되는 예외의 최상위 클래스
 */
public abstract class AccountBusinessException extends BusinessException {

    public AccountBusinessException(ErrorCode errorCode) {
        super(Domain.ACCOUNT, errorCode);
    }

    public AccountBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.ACCOUNT, errorCode);
    }
}
