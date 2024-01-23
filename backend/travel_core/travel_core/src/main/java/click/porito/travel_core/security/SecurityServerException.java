package click.porito.travel_core.security;

import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.global.exception.ErrorCode;
import click.porito.travel_core.global.exception.ServerException;

public class SecurityServerException extends ServerException {
    public SecurityServerException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    public SecurityServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
