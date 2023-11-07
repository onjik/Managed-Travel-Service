package click.porito.modular_travel.image.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
@Order(0)
@ControllerAdvice
public class ImageExceptionHandler {
    @ExceptionHandler(ImageTypeNotSupportedException.class)
    public ResponseEntity<Map<String,String>> handleImageTypeNotSupportedException(ImageTypeNotSupportedException e) {
        //Bad request
        final Map<String, String> message = Map.of("message", "Image Type Not Supported");
        return ResponseEntity.badRequest().body(message);
    }
}
