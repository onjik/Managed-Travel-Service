package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_distribution;

import click.porito.common.trace.TraceContext;
import click.porito.common.trace.TraceContextStrategy;

public class ThreadLocalTraceContextStrategy implements TraceContextStrategy {
    @Override
    public TraceContext getTraceContext() {
        return TraceContextHolder.getContext();
    }
}
