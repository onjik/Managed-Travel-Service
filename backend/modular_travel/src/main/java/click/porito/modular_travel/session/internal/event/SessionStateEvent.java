package click.porito.modular_travel.session.internal.event;

public abstract class SessionStateEvent extends SessionEvent {
    private final String sessionId;

    public SessionStateEvent(Object source, String sessionId) {
        super(source);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

}
