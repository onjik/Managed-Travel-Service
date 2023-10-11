package click.porito.modular_travel.session.internal.event;

import java.util.Date;


public class SessionExpiredEvent extends SessionStateEvent {

    private final Date lastAccessedTime;
    public SessionExpiredEvent(Object source, String sessionId, Date lastAccessedTime) {
        super(source, sessionId);
        this.lastAccessedTime = lastAccessedTime;
    }

    public Date getLastAccessedTime() {
        return lastAccessedTime;
    }
}
