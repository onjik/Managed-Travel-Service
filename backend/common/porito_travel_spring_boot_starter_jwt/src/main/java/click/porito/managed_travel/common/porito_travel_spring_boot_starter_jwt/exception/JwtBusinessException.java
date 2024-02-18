package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ErrorCode;

public class JwtBusinessException extends BusinessException {
    public JwtBusinessException(ErrorCode errorCode) {
        super(Domain.SECURITY, errorCode);
    }

    public JwtBusinessException(Throwable cause,ErrorCode errorCode) {
        super(cause, Domain.SECURITY, errorCode);
    }
}
