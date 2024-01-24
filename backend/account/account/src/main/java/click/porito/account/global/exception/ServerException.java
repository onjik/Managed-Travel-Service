package click.porito.account.global.exception;

import click.porito.account.global.constant.Domain;

public abstract non-sealed class ServerException extends ServerThrownException {
    public ServerException(Domain domain, ErrorCode errorCode) {
        super(domain, errorCode);
    }

    public ServerException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
