package click.porito.travel_core.place.dao;

import org.springframework.dao.UncategorizedDataAccessException;

public class UnexpectedExternalApiException extends UncategorizedDataAccessException {
    public UnexpectedExternalApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
