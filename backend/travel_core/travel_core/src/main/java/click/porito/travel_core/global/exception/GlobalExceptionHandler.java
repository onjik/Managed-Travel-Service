package click.porito.travel_core.global.exception;

import click.porito.travel_core.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseBody> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.debug("handleConstraintViolationException", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<FieldError> fieldErrors = constraintViolations.stream()
                .flatMap(violation -> {
                    String[] propertyPath = violation.getPropertyPath().toString().split("\\.");
                    String field = propertyPath[propertyPath.length - 1];
                    String value = violation.getInvalidValue() == null ? "" : violation.getInvalidValue().toString();
                    String reason = violation.getMessage();
                    return FieldError.of(field, value, reason).stream();
                })
                .toList();
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponseBody body = ErrorResponseBody.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatusValue())
                .debugDescription(errorCode.getDebugDescription())
                .message(e.getMessage())
                .errors(fieldErrors)
                .build();

        return ResponseEntity.badRequest()
                .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBody> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.debug("handleIllegalArgumentException", e);
        return ResponseEntity.badRequest()
                .body(ErrorResponseBody.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage(), null));
    }

    @ExceptionHandler(Mapper.MapperException.class)
    public ResponseEntity<ErrorResponseBody> handleMapperException(Mapper.MapperException e, HttpServletRequest request) {
        log.debug("handleMapperException", e);
        return ResponseEntity.badRequest()
                .body(ErrorResponseBody.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage(), null));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponseBody> handleBusinessException(ServerException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusValue())
                .body(createErrorBody(e));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseBody> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusValue())
                .body(createErrorBody(e));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseBody> handleThrowable(Throwable e) {
        log.error("!!UNHANDLED EXCEPTION!!", e);
        ErrorResponseBody body = ErrorResponseBody.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("unhandled exception")
                .debugDescription(e.getClass().getSimpleName() + ": " + e.getMessage())
                .build();
        return ResponseEntity.internalServerError()
                .body(body);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        int statusCodeValue = statusCode.value();
        String message = "error";
        ErrorResponseBody newBody = ErrorResponseBody.builder()
                .status(statusCodeValue)
                .message(message)
                .debugDescription(ex.getClass().getSimpleName() + ": " + ex.getMessage())
                .build();
        return super.handleExceptionInternal(ex, newBody, headers, statusCode, request);
    }

    protected ErrorResponseBody createErrorBody(ServerThrownException businessException) {
        final ErrorCode errorCode = businessException.getErrorCode();//never null
        final String code = errorCode.getCode();
        final int status = errorCode.getStatusValue();
        final String debugDescription = errorCode.getDebugDescription();
        final List<FieldError> fieldErrors = businessException.getFieldErrors(); // nullable
        final Map<String, Object> detailsToExpose = businessException.getDetailsToExpose(); // nullable

        // ErrorCode enum 으로 다국어 메시지 조회 - nullable
        final String message = messageSource.getMessage(errorCode.name(), null, null, Locale.getDefault());

        // ErrorResponseBody 객체 생성
        return ErrorResponseBody.builder()
                .code(code)
                .status(status)
                .debugDescription(debugDescription)
                .message(message)
                .errors(fieldErrors)
                .details(detailsToExpose)
                .build();
    }


}
