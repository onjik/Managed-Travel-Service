package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.event;

import org.springframework.context.ApplicationEvent;

public class JwtAuthenticationSuccessEvent extends ApplicationEvent {
    private String userId;
    public JwtAuthenticationSuccessEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }
}
