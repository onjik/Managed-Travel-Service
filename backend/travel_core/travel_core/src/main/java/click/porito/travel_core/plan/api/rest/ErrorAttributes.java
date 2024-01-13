package click.porito.travel_core.plan.api.rest;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public record ErrorAttributes(
        ZonedDateTime timestamp,
        int status,
        String error,
        String exception,
        String message,
        String path,
        Map<String, Object> details
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ZonedDateTime timestamp;
        private HttpStatus status;
        private Throwable exception;
        private String message;
        private String path;
        private Map<String, Object> details = new HashMap<>();

        private Builder() {
        }

        public Builder timestamp(ZonedDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }


        public Builder exception(Throwable exception) {
            this.exception = exception;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder addDetail(String key, Object value) {
            this.details.put(key, value);
            return this;
        }


        public ErrorAttributes build() {
            return new ErrorAttributes(timestamp, status.value(), status.getReasonPhrase(), exception.getClass().getName(), message, path, details);
        }

    }
}
