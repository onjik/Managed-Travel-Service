package click.porito.travel_core.plan.api.rest;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WayPointUpdateRequest.class),
        @JsonSubTypes.Type(value = DayUpdateRequest.class)
})
public sealed interface RouteComponentRequest permits DayUpdateRequest, WayPointUpdateRequest {

    String id();
}
