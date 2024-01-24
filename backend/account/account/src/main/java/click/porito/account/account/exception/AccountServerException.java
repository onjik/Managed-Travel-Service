package click.porito.account.account.exception;

import click.porito.account.global.constant.Domain;
import click.porito.account.global.exception.ErrorCode;
import click.porito.account.global.exception.ServerException;

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
