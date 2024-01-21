package click.porito.travel_core.security;

import click.porito.travel_core.global.exception.ErrorCode;

public class ResourceNotFoundException extends SecurityBusinessException{
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
