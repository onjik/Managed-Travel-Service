package click.porito.managed_travel.place.domain.request.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class OperationTimePutRequest {
    @NotNull
    private Long placeId;
    private Long operationTimeId; // creat if null
    @NotNull
    private LocalTime startDate;
    @NotNull
    private LocalTime endDate;
    private List<@Valid DayOperationTimeUpdateRequest> dayOperationTimes;
}
