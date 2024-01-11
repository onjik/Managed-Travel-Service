package click.porito.travel_core.place;

import org.springframework.core.NestedRuntimeException;

public class PlaceServiceException extends NestedRuntimeException {
    public PlaceServiceException(String msg) {
        super(msg);
    }

    public PlaceServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
