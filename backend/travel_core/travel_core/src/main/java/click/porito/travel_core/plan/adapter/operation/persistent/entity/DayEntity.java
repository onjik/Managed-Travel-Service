package click.porito.travel_core.plan.adapter.operation.persistent.entity;

import lombok.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class DayEntity implements EntityRouteComponent {
    public DayEntity(UUID dayId) {
        this.dayId = dayId;
    }

    private UUID dayId;
    private List<WayPointEntity> wayPoints = new ArrayList<>();

    @Builder
    public DayEntity(UUID dayId, List<WayPointEntity> wayPoints) {
        Assert.notNull(dayId, "dayId must not be null");
        Assert.notNull(wayPoints, "wayPoints must not be null");
        this.dayId = dayId;
        this.wayPoints = wayPoints;
    }

    @Override
    public String getId() {
        return dayId != null ? dayId.toString() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayEntity dayEntity = (DayEntity) o;
        return Objects.equals(getDayId(), dayEntity.getDayId()) && Objects.equals(getWayPoints(), dayEntity.getWayPoints());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDayId(), getWayPoints());
    }
}
