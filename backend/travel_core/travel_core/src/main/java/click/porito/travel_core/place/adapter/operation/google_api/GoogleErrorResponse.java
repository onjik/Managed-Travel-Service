package click.porito.travel_core.place.adapter.operation.google_api;

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
