package click.porito.account.security;

import click.porito.account.global.constant.Domain;
import click.porito.account.global.exception.ErrorCode;
import click.porito.account.global.exception.ServerException;

public class SecurityServerException extends ServerException {
    public SecurityServerException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    public SecurityServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
