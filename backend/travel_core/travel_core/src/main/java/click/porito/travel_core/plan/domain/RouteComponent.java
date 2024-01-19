package click.porito.travel_core.plan.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_type")
@JsonSubTypes({
        @Type(value = WayPoint.class, name = "WAYPOINT"),
        @Type(value = Day.class, name = "DAY")
})
public sealed interface RouteComponent permits WayPoint, Day {
}
