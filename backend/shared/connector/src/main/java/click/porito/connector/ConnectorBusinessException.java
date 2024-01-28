package click.porito.connector;

import click.porito.common.exception.BusinessException;
import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ErrorCode;

public class ConnectorBusinessException extends BusinessException {
    public ConnectorBusinessException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
