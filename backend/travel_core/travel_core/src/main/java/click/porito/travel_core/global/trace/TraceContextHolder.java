package click.porito.travel_core.global.trace;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

public class TraceContextHolder {
    private static final ThreadLocal<TraceContext> correlationContext =
            new ThreadLocal<>();

    public static TraceContext getContext() {
        TraceContext context = correlationContext.get();
        if (context == null) {
            context = createEmptyContext();
            correlationContext.set(context);
        }
        return context;
    }

    public static void setContext(TraceContext context) {
        Assert.notNull(context, "Only non-null TraceContext instances are permitted");
        correlationContext.set(context);
    }

    public static TraceContext createEmptyContext() {
        SecurityContextHolder.clearContext();
        return new TraceContext();
    }

    public static void clearContext(){
        correlationContext.remove();
    }

}
