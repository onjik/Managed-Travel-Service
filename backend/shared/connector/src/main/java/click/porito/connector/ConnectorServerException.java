package click.porito.connector;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ServerException;
import click.porito.common.exception.common.ErrorCode;

public class ConnectorServerException extends ServerException {
    public ConnectorServerException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
