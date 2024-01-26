package click.porito.account_common.event;

import org.springframework.context.ApplicationEvent;

public class JwtAuthenticationFailedEvent extends ApplicationEvent {
    private Exception exception;
    public JwtAuthenticationFailedEvent(Object source, Exception exception) {
        super(source);
        this.exception = exception;
    }
}
