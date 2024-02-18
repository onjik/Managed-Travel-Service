package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.event;

import org.springframework.context.ApplicationEvent;

public class JwtAuthenticationFailedEvent extends ApplicationEvent {
    private Exception exception;
    public JwtAuthenticationFailedEvent(Object source, Exception exception) {
        super(source);
        this.exception = exception;
    }
}
