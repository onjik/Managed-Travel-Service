package click.porito.travel_core_service.plan.operation.adapter.persistent.entity;

import lombok.*;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class WayPointEntity implements EntityRouteComponent {
    private UUID wayPointId;
    private String placeId;
    private String memo;
    private LocalTime time;

    public WayPointEntity(UUID wayPointId, String placeId) {
        this.wayPointId = wayPointId;
        this.placeId = placeId;
    }

    @Builder
    public WayPointEntity(final UUID wayPointId, final String placeId, String memo, LocalTime time) {
        Assert.notNull(wayPointId, "wayPointId must not be null");
        Assert.notNull(placeId, "placeId must not be null");
        this.wayPointId = wayPointId;
        this.placeId = placeId;
        this.memo = memo;
        this.time = time;
    }

    @Override
    public String getId() {
        return wayPointId != null ? wayPointId.toString() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WayPointEntity that = (WayPointEntity) o;
        return Objects.equals(getWayPointId(), that.getWayPointId()) && Objects.equals(getPlaceId(), that.getPlaceId()) && Objects.equals(getMemo(), that.getMemo()) && Objects.equals(getTime(), that.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWayPointId(), getPlaceId(), getMemo(), getTime());
    }
}
