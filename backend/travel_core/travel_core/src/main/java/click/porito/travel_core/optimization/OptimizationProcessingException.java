package click.porito.travel_core.optimization;

import org.springframework.core.NestedRuntimeException;

public class OptimizationProcessingException extends NestedRuntimeException {
    public OptimizationProcessingException(String msg) {
        super(msg);
    }

    public OptimizationProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
