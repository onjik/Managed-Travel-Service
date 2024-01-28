package click.porito.common.exception;


import click.porito.common.exception.common.ErrorCode;

public abstract non-sealed class ServerException extends ServerThrownException {
    public ServerException(Domain domain, ErrorCode errorCode) {
        super(domain, errorCode);
    }

    public ServerException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
