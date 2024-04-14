package click.porito.managed_travel.place.domain.request.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DayOperationTimeCreateRequest {
    @NotNull
    private LocalTime startDate;
    @NotNull
    private LocalTime endDate;
    private Boolean nextDayLinked = false;
}
