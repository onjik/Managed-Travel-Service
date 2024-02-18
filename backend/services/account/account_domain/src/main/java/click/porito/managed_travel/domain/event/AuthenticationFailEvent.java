package click.porito.managed_travel.domain.event;


public record AuthenticationFailEvent(
        String requestIp,
        String requestUrl,
        String exceptionName,
        String message
) {
}
