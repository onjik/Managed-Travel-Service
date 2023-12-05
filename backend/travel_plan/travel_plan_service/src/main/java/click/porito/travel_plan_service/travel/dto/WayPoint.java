package click.porito.travel_plan_service.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter @Setter
public class WayPoint {
    private final UUID id;
    private final String placeId;
    private String memo;
    private LocalTime visitTime;

    public WayPoint(UUID id, String placeId, String memo, LocalTime visitTime) {
        this.id = id;
        this.placeId = placeId;
        this.memo = memo;
        this.visitTime = visitTime;
    }
}
