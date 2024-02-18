package click.porito.managed_travel.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Getter @Setter
@EqualsAndHashCode(of = {"waypointId", "placeId"})
@Builder
public non-sealed class WayPoint implements RouteComponent {
    @JsonProperty("wayPointId")
    private final String waypointId;
    @JsonProperty("placeId")
    private String placeId;
    @JsonInclude(NON_NULL)
    @JsonProperty("memo")
    private String memo;
    @JsonInclude(NON_NULL)
    @JsonProperty("time")
    private LocalTime time;

    protected WayPoint(
            @JsonProperty("wayPointId")
            String waypointId,
            @JsonProperty("placeId")
            String placeId,
            @JsonProperty("memo")
            String memo,
            @JsonProperty("time")
            LocalTime time
    ) {
        this.waypointId = waypointId;
        this.placeId = placeId;
        this.memo = memo;
        this.time = time;
    }

    public static WayPoint createWayPoint(String placeId) {
        return new WayPoint(UUID.randomUUID().toString(), placeId, null, null);
    }

    public static WayPoint createWayPoint(String waypointId, String placeId) {
        return new WayPoint(waypointId, placeId, null, null);
    }

    @Override
    public String toString() {
        return "WayPoint[" +
                "waypointId=" + waypointId + ", " +
                "placeId=" + placeId + ", " +
                "memo=" + memo + ", " +
                "time=" + time + ']';
    }

}
