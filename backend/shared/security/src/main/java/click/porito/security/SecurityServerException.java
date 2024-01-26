package click.porito.security;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCode;
import click.porito.common.exception.ServerException;

public class SecurityServerException extends ServerException {
    public SecurityServerException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    public SecurityServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
