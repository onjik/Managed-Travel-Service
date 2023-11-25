package click.porito.modular_travel.security.event;

public record AuthenticationSuccessEvent (
        String userId,
        AuthenticationMethod method
){
}
