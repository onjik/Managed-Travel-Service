package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ServerException;
import click.porito.common.exception.common.ErrorCode;

public class ConnectorServerException extends ServerException {
    public ConnectorServerException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super(cause, domain, errorCode);
    }
}
