package click.porito.common.trace;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.Optional;

@Setter
@EqualsAndHashCode(of = {"correlationId"})
public final class TraceContext {
    private String correlationId;
    private String jwtToken;

    public TraceContext() {
    }

    public Optional<String> getCorrelationId() {
        return Optional.ofNullable(correlationId);
    }

    public Optional<String> getJwtToken() {
        return Optional.ofNullable(jwtToken);
    }
}
