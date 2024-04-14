package click.porito.managed_travel.place.domain.request.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class OperationTimeCreateRequest {
    @NotNull
    private Long placeId;
    @NotNull
    private LocalTime startDate;
    @NotNull
    private LocalTime endDate;
    @NotEmpty
    private List<@Valid DayOperationTimeCreateRequest> dayOperationTimes;
}
