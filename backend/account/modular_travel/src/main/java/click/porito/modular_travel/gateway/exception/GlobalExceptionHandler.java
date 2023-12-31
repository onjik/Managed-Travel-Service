package click.porito.modular_travel.gateway.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "{\"message\":\"Internal Server Error\"}";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.debug("ConstraintViolationException", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> invalidFields = constraintViolations.stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .toList();
        Map<String, List<String>> body = Map.of("invalidFields", invalidFields);
        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e) {
        log.error("Unhandled ObjectOptimisticLockingFailureException", e);
        Map<String, String> body = Map.of(
                "message", "Temporary Server Error : Conflict"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleThrowable(Throwable e) {
        log.error("Unhandled Throwable", e);
        return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR_MESSAGE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.debug("MethodArgumentNotValidException", ex);
        List<String> invalidFields = ex.getFieldErrors().stream()
                .map(FieldError::getField)
                .toList();
        Map<String, List<String>> body = Map.of("invalidFields", invalidFields);
        return ResponseEntity.badRequest().body(body);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.debug("HttpRequestMethodNotSupportedException", ex);
        String method = ex.getMethod();
        var message = Map.of("message", String.format("Method %s is not supported", method));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(message);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.debug("HttpMediaTypeNotSupportedException", ex);
        List<MediaType> supportedMediaTypes = ex.getSupportedMediaTypes();
        Map<String, List<MediaType>> body = Map.of("supportedMediaTypes", supportedMediaTypes);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.debug("HttpMediaTypeNotAcceptableException", ex);
        List<MediaType> supportedMediaTypes = ex.getSupportedMediaTypes();
        Map<String, List<MediaType>> body = Map.of("supportedMediaTypes", supportedMediaTypes);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String variableName = ex.getVariableName();
        var message = Map.of("message", "Missing Path Variable " + variableName);
        return ResponseEntity.badRequest().body(message);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String parameterName = ex.getParameterName();
        var message = Map.of("message", "Missing Request Parameter " + parameterName);
        return ResponseEntity.badRequest().body(message);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String requestPartName = ex.getRequestPartName();
        var message = Map.of("message", "Missing Request Part " + requestPartName);
        return ResponseEntity.badRequest().body(message);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("AsyncRequestTimeoutException", ex);
        return super.handleAsyncRequestTimeoutException(ex, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("ConversionNotSupportedException", ex);
        return super.handleConversionNotSupported(ex, headers, status, request);
    }

}
