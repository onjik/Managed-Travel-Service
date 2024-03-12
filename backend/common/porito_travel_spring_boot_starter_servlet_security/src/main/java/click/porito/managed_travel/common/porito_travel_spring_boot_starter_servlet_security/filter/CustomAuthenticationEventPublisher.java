package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.filter;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.event.JwtAuthenticationFailedEvent;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.event.JwtAuthenticationSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class CustomAuthenticationEventPublisher implements AuthenticationEventPublisher, ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;
    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        if (this.eventPublisher != null) {
            String userId = authentication.getName();
            this.eventPublisher.publishEvent(new JwtAuthenticationSuccessEvent(this,userId));
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new JwtAuthenticationFailedEvent(this, exception));
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
