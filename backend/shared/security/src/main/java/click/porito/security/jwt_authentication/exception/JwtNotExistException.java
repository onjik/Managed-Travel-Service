package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCode;
import click.porito.security.SecurityBusinessException;

public class JwtNotExistException extends SecurityBusinessException {
    public JwtNotExistException() {
        super(ErrorCode.JWT_NOT_EXIST);
    }
}
