package click.porito.modular_travel.session.internal.event;

public class InvalidSessionEvent extends SessionEvent implements NetworkContextAware {

    private final NetworkContextAware.Context networkEventContext;

    public InvalidSessionEvent(Object source, Context networkEventContext) {
        super(source);
        this.networkEventContext = networkEventContext;
    }

    @Override
    public Context getNetworkEventContext() {
        return this.networkEventContext;
    }
}
