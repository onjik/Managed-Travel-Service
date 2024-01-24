package click.porito.account.global.trace;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"correlationId", "authToken"})
public final class TraceContext {
    private String correlationId;
    private String authToken;

    public TraceContext(String correlationId, String authToken) {
        this.correlationId = correlationId;
        this.authToken = authToken;
    }

    public TraceContext() {
    }
}
