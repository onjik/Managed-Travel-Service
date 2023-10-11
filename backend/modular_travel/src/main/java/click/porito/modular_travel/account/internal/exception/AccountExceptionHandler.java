package click.porito.modular_travel.account.internal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Account 도메인 내에서 발생한 예외를 처리하는 핸들러
 */
@Slf4j
@ControllerAdvice(basePackages = "click.porito.modular_travel.account")
public class AccountExceptionHandler {

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
        log.error("Invalid Authentication", e);
        SecurityContextHolder.clearContext(); // 인증 정보를 무효화 시킨다.
        log.debug("SecurityContextHolder cleared");
        final Map<String, String> message = Map.of("message", "Invalid Authentication");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(ImageTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleImageTypeNotSupportedException(ImageTypeNotSupportedException e) {
        log.debug("Image Type Not Supported", e);
        final Map<String, String> message = Map.of("message", "Image Type Not Supported");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException e) {
        log.trace("User Not Found", e);
        final Map<String, String> message = Map.of("message", "User Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(NameCreationIoException.class)
    public ResponseEntity<Map<String, String>> handleNameCreationIoException(NameCreationIoException e) {
        log.error("Name Creation Io Exception", e);
        final Map<String, String> message = Map.of("message", "Name Creation Failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
