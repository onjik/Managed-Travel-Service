package click.porito.managed_travel.place.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class OperationTime {
    private Long operationTimeId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Data
    static class DayOperationTime {
        private Long dayOperationTimeId;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean nextDayLinked;

    }

}
