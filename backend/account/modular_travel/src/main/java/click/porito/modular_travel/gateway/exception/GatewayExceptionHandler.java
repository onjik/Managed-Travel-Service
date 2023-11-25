package click.porito.modular_travel.gateway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GatewayExceptionHandler {
    @ExceptionHandler(ResponseEntityNotFoundException.class)
    ResponseEntity<Void> handle(ResponseEntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
