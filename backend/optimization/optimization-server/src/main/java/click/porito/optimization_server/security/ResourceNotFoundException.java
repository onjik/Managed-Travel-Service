package click.porito.optimization_server.security;


import click.porito.optimization_server.global.exception.ErrorCode;

public class ResourceNotFoundException extends SecurityBusinessException{
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
