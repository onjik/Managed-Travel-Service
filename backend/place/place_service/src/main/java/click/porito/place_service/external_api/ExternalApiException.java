package click.porito.place_service.external_api;

public abstract class ExternalApiException extends RuntimeException{
    public abstract int getStatusCode();
    public abstract String getStatus();
    public ExternalApiException(String message) {
        super(message);
    }
}
