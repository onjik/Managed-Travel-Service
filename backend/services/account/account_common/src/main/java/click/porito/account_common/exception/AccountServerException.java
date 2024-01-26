package click.porito.account_common.exception;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCode;
import click.porito.common.exception.ServerException;

/**
 * AccountEntity 도메인에서 예상치 못한 서버 에러에 대한 상위 클래스
 */
public final class AccountServerException extends ServerException {

    public AccountServerException(ErrorCode errorCode) {
        super(Domain.ACCOUNT, errorCode);
    }

    public AccountServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.ACCOUNT, errorCode);
    }
}
