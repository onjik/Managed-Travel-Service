package click.porito.managed_travel.place.place_service.google_api;

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
