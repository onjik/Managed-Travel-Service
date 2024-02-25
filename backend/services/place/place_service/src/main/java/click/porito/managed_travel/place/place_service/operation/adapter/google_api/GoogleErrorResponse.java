package click.porito.managed_travel.place.place_service.operation.adapter.google_api;

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
