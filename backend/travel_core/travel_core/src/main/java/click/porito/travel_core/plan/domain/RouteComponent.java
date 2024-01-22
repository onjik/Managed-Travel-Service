package click.porito.travel_core.plan.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @Type(value = WayPoint.class),
        @Type(value = Day.class)
})
public sealed interface RouteComponent permits WayPoint, Day {
}
