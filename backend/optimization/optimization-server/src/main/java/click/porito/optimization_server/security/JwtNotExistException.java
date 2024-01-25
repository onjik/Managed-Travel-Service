package click.porito.optimization_server.security;


import click.porito.optimization_server.global.exception.ErrorCode;

public class JwtNotExistException extends SecurityBusinessException{
    public JwtNotExistException() {
        super(ErrorCode.JWT_NOT_EXIST);
    }
}
