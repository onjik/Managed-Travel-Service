package click.porito.optimization_server.security;

import click.porito.optimization_server.global.constant.Domain;
import click.porito.optimization_server.global.exception.BusinessException;
import click.porito.optimization_server.global.exception.ErrorCode;

public class SecurityBusinessException extends BusinessException {
    public SecurityBusinessException(ErrorCode errorCode) {
        super(Domain.SECURITY, errorCode);
    }

    public SecurityBusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
