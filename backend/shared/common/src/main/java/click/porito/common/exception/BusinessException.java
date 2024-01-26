package click.porito.common.exception;

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
