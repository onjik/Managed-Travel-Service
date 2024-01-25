package click.porito.optimization_server.security;

import click.porito.optimization_server.global.constant.Domain;
import click.porito.optimization_server.global.exception.ErrorCode;
import click.porito.optimization_server.global.exception.ServerException;

public class SecurityServerException extends ServerException {
    public SecurityServerException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    public SecurityServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
