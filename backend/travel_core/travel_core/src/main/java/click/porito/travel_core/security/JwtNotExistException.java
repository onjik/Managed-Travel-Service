package click.porito.travel_core.security;

import click.porito.travel_core.global.exception.ErrorCode;

public class JwtNotExistException extends SecurityBusinessException{
    public JwtNotExistException() {
        super(ErrorCode.JWT_NOT_EXIST);
    }
}
