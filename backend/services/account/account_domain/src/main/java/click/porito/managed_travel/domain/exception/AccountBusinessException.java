package click.porito.managed_travel.domain.exception;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ErrorCode;

/**
 * AccountEntity 도메인 내에서 발생되고 처리되는 예외의 최상위 클래스
 */
public class AccountBusinessException extends BusinessException {

    public AccountBusinessException(ErrorCode errorCode) {
        super(Domain.ACCOUNT, errorCode);
    }

    public AccountBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.ACCOUNT, errorCode);
    }
}
