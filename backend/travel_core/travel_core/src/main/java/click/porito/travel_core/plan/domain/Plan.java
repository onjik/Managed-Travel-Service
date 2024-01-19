package click.porito.travel_core.plan.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public final class Plan {
    private String planId;
    private String title;
    @Nullable
    private LocalDate startDate;
    private Instant createdAt;
    private Instant updatedAt;
    private String version;
    private String ownerId;
    private List<RouteComponent> route;

    public Plan(
            String planId,
            String title,
            @Nullable
            LocalDate startDate,
            Instant createdAt,
            Instant updatedAt,
            String version,
            String ownerId,
            List<RouteComponent> route
    ) {
        this.planId = planId;
        this.title = title;
        this.startDate = startDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.ownerId = ownerId;
        this.route = route;
    }

}
