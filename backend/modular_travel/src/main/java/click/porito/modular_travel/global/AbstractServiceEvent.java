package click.porito.modular_travel.global;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.time.Instant;

@Getter
public abstract class AbstractServiceEvent extends ApplicationEvent {

    public AbstractServiceEvent(Object source) {
        super(source, Clock.systemUTC());
    }

    public Instant getTimeStamp(){
        return Instant.ofEpochMilli(super.getTimestamp());
    }
}
