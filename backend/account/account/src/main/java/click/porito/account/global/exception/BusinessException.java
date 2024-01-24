package click.porito.account.global.exception;

import click.porito.account.global.constant.Domain;
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
