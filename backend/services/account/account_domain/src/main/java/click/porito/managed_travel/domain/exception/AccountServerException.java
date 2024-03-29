package click.porito.managed_travel.domain.exception;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ServerException;
import click.porito.common.exception.common.ErrorCode;

/**
 * AccountEntity 도메인에서 예상치 못한 서버 에러에 대한 상위 클래스
 */
public class AccountServerException extends ServerException {

    public AccountServerException(ErrorCode errorCode) {
        super(Domain.ACCOUNT, errorCode);
    }

    public AccountServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.ACCOUNT, errorCode);
    }
}
