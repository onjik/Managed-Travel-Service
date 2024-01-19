package click.porito.travel_core.global.exception;

import click.porito.travel_core.global.constant.Domain;
import lombok.Getter;

@Getter
public abstract non-sealed class BusinessException extends ServerThrownException {
    public BusinessException(Domain domain, ErrorCode errorCode) {
        super(domain, errorCode);
    }

    public BusinessException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
