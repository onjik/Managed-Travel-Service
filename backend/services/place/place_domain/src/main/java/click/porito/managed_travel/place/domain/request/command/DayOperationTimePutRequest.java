package click.porito.managed_travel.place.domain.request.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

/**
 * 덮어쓰기 방식으로(PUT) 동작하는 운영시간 수정 요청
 */
@Data
public class DayOperationTimePutRequest {
    private Long dayOperationTimeId;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    private Boolean nextDayLinked = false;
}
