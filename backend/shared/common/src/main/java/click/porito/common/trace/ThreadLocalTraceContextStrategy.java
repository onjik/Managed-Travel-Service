package click.porito.common.trace;

public class ThreadLocalTraceContextStrategy implements TraceContextStrategy {
    @Override
    public TraceContext getTraceContext() {
        return TraceContextHolder.getContext();
    }
}
