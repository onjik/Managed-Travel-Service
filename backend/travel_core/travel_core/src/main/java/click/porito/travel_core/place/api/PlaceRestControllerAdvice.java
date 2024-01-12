package click.porito.travel_core.place.api;

import click.porito.travel_core.AbstractRestExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackageClasses = PlaceRestApi.class)
public class PlaceRestControllerAdvice extends AbstractRestExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        var errors = ex.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", violation.getMessage()
                ))
                .toList();
        return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("errors", errors));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getFieldErrors().stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()
                ))
                .toList();
        return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("errors", errors));
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getValueResults().stream()
                .map(result -> Map.of(
                        "field", result.getMethodParameter().getParameterName(),
                        "message", result.getResolvableErrors().stream()
                                .map(MessageSourceResolvable::getDefaultMessage)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(", "))
                ))
                .toList();
        return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("errors", errors));
    }

}
