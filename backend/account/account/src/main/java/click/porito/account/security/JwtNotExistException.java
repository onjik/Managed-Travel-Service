package click.porito.account.security;


import click.porito.account.global.exception.ErrorCode;

public class JwtNotExistException extends SecurityBusinessException{
    public JwtNotExistException() {
        super(ErrorCode.JWT_NOT_EXIST);
    }
}
