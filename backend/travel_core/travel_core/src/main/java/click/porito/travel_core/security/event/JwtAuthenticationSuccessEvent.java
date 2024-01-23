package click.porito.travel_core.security.event;

import org.springframework.context.ApplicationEvent;

public class JwtAuthenticationSuccessEvent extends ApplicationEvent {
    private String userId;
    public JwtAuthenticationSuccessEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }
}
