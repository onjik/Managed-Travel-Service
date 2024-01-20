package click.porito.travel_core.plan.adapter.operation.persistent.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@Document(collection = "plan")
public class PlanEntity {
    @Id
    private String planId;
    private String title;
    private LocalDate startDate;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    @Version
    private Long version;
    private String ownerId;
    private List<EntityRouteComponent> route = new ArrayList<>();

    @Builder
    public PlanEntity(String planId, String title, LocalDate startDate, Instant createdAt, Instant updatedAt, Long version, String ownerId, List<EntityRouteComponent> route) {
        this.planId = planId;
        this.title = title;
        this.startDate = startDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.ownerId = ownerId;
        this.route = route == null ? new ArrayList<>() : route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanEntity that = (PlanEntity) o;
        return Objects.equals(getPlanId(), that.getPlanId()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getUpdatedAt(), that.getUpdatedAt()) && Objects.equals(getVersion(), that.getVersion()) && Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getRoute(), that.getRoute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlanId(), getTitle(), getStartDate(), getCreatedAt(), getUpdatedAt(), getVersion(), getOwnerId(), getRoute());
    }
}
