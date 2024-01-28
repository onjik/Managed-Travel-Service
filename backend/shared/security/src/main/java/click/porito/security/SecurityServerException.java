package click.porito.security;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ServerException;
import click.porito.common.exception.common.ErrorCode;

public class SecurityServerException extends ServerException {
    public SecurityServerException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    public SecurityServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
