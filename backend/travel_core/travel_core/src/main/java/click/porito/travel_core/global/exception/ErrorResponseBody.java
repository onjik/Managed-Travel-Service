package click.porito.travel_core.global.exception;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * of 메서드를 통해 ErrorResponseBody 응답 형식 지정
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseBody {

    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private String code;
    private int status;
    private String debugDescription; //FOR DEBUG ONLY
    private String message; //OPTIONAL
    private List<FieldError> errors;
    private Map<String, Object> details;

    @Builder
    public ErrorResponseBody(String code, int status, String message, String debugDescription, List<FieldError> errors, Map<String, Object> details) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.debugDescription = debugDescription;
        this.errors = errors;
        this.details = details;
    }

    public static ErrorResponseBody of(@NonNull ErrorCode errorCode, @Nullable String message, @Nullable Map<String, Object> detailsToExpose) {
        return ErrorResponseBody.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatus().value())
                .message(message)
                .details(detailsToExpose != null ? detailsToExpose : Map.of())
                .build();
    }



}
