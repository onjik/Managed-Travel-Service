package click.porito.place_service.external_api.google_api;

import click.porito.place_service.external_api.ExternalApiException;

public abstract class GoogleApiException extends ExternalApiException {

    private final GoogleErrorResponse errorResponse;

    public GoogleApiException(GoogleErrorResponse googleErrorResponse) {
        super(googleErrorResponse.error().message());
        this.errorResponse = googleErrorResponse;
    }

    public static GoogleApiException of(GoogleErrorResponse errorResponse) {
        if (errorResponse.error().code() == 400 && errorResponse.error().status().equals("INVALID_ARGUMENT")) {
            return new GoogleBadRequestException(errorResponse);
        } else if (errorResponse.error().code() == 500) {
            return new GoogleServerErrorException(errorResponse);
        } else {
            return new GoogleUnknownException(errorResponse);
        }
    }

    @Override
    public int getStatusCode() {
        return this.errorResponse.error().code();
    }

    @Override
    public String getStatus() {
        return this.errorResponse.error().status();
    }

    public static class GoogleBadRequestException extends GoogleApiException {
        private GoogleBadRequestException(GoogleErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public static class GoogleServerErrorException extends GoogleApiException {
        private GoogleServerErrorException(GoogleErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public static class GoogleUnknownException extends GoogleApiException {
        private GoogleUnknownException(GoogleErrorResponse errorResponse) {
            super(errorResponse);
        }
    }


    public record GoogleErrorResponse(
            Error error

    ) {
        public record Error(
                int code,
                String message,
                String status
        ) {
        }
    }
}
