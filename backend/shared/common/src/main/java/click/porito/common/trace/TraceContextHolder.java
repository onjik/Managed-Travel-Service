package click.porito.common.trace;

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

    private static TraceContext createEmptyContext() {
        TraceContext context = new TraceContext();
        correlationContext.set(context);
        return context;
    }

    public static void clearContext(){
        correlationContext.remove();
    }

    public static boolean hasContext() {
        return correlationContext.get() != null;
    }

}
