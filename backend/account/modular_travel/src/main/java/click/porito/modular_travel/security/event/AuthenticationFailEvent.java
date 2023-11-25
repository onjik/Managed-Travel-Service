package click.porito.modular_travel.security.event;

public record AuthenticationFailEvent(
        String userId,
        AuthenticationMethod method,
        FailedReason reason,
        String message
) {
}
