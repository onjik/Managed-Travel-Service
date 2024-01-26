package click.porito.plan_common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Data
@EqualsAndHashCode(of = {"planId", "createdAt"})
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
            @JsonProperty("planId")
            String planId,
            @JsonProperty("title")
            String title,
            @JsonProperty("startDate")
            LocalDate startDate,
            @JsonProperty("createdAt")
            Instant createdAt,
            @JsonProperty("updatedAt")
            Instant updatedAt,
            @JsonProperty("version")
            String version,
            @JsonProperty("ownerId")
            String ownerId,
            @JsonProperty("route")
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
