package click.porito.travel_core;

import click.porito.travel_core.plan.api.rest.ErrorAttributes;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

public abstract class AbstractRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorAttributes> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ErrorAttributes attributes = ErrorAttributes.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .message("Illegal Argument")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(attributes);
    }
}
