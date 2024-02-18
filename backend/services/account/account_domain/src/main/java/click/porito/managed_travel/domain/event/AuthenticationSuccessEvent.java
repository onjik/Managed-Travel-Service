package click.porito.managed_travel.domain.event;


public record AuthenticationSuccessEvent (
        String userId,
        String requestIp,
        String requestUrl
){
}
