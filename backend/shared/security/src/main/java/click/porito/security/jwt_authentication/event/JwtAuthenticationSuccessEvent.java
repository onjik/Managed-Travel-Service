package click.porito.security.jwt_authentication.event;

import org.springframework.context.ApplicationEvent;

public class JwtAuthenticationSuccessEvent extends ApplicationEvent {
    private String userId;
    public JwtAuthenticationSuccessEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }
}
