package click.porito.account.security;

import click.porito.account.global.constant.Domain;
import click.porito.account.global.exception.BusinessException;
import click.porito.account.global.exception.ErrorCode;

public class SecurityBusinessException extends BusinessException {
    public SecurityBusinessException(ErrorCode errorCode) {
        super(Domain.SECURITY, errorCode);
    }

    public SecurityBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
