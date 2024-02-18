package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception;


import click.porito.common.exception.ErrorCodes;

public class JwtNotExistException extends JwtBusinessException {
    public JwtNotExistException() {
        super(ErrorCodes.JWT_NOT_EXIST);
    }
}
