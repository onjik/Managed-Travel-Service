package click.porito.managed_travel.place.domain.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationTimeView {
    private Long operationTimeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DayOperationTime> dayOperationTimes;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DayOperationTime {
        private Long dayOperationTimeId;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean nextDayLinked;

    }

}
