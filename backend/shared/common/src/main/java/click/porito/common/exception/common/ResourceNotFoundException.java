package click.porito.common.exception.common;


import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;

public class ResourceNotFoundException extends CommonBusinessException {
    public ResourceNotFoundException(Domain domain) {
        super(domain, ErrorCodes.RESOURCE_NOT_FOUND);
    }
}
