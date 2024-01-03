package click.porito.travel_plan_service.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_type")
@JsonSubTypes({
        @Type(value = WayPointView.class, name = "WAYPOINT"),
        @Type(value = DayView.class, name = "DAY")
})
public sealed interface RouteComponentView permits WayPointView, DayView {
}
