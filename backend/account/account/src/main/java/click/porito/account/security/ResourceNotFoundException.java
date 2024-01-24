package click.porito.account.security;


import click.porito.account.global.exception.ErrorCode;

public class ResourceNotFoundException extends SecurityBusinessException{
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
