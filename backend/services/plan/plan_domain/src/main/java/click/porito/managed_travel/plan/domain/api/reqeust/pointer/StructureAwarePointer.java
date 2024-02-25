package click.porito.managed_travel.plan.domain.api.reqeust.pointer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 여행지 Component 를 가리키는 포인터
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(StructureAwareDayPointer.class),
        @JsonSubTypes.Type(StructureAwareWaypointPointer.class)
})
public sealed interface StructureAwarePointer permits StructureAwareDayPointer, StructureAwareWaypointPointer {

}
