package click.porito.managed_travel.place.domain.request.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OperationTimePutRequest {
    @NotNull
    private Long placeId;
    private Long operationTimeId; // creat if null
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotEmpty
    private List<@Valid DayOperationTimePutRequest> dayOperationTimes;
}
