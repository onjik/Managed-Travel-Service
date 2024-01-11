package click.porito.travel_core.place.dao.google_api;

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
