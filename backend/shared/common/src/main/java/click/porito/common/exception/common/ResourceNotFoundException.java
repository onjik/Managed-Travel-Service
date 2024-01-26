package click.porito.common.exception.common;


import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCode;

public class ResourceNotFoundException extends CommonBusinessException {
    public ResourceNotFoundException(Domain domain) {
        super(domain, ErrorCode.RESOURCE_NOT_FOUND);
    }
}
