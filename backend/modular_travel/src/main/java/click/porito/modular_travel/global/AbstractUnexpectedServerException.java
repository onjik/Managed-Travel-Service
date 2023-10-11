package click.porito.modular_travel.global;

import org.springframework.web.server.ServerErrorException;

/**
 * 예상치 못한 서버 에러에 대한 상위 클래스
 */
public abstract class AbstractUnexpectedServerException extends ServerErrorException implements ServiceException {
    public abstract ErrorCode getErrorCode();

    protected AbstractUnexpectedServerException(String reason) {
        super(reason, null);
    }

    protected AbstractUnexpectedServerException() {
        super("unexpected server error", null);
    }

}
