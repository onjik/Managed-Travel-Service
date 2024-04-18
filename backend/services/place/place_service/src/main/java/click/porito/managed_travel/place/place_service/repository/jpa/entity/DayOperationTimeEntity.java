package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import click.porito.managed_travel.place.domain.view.OperationTimeView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalTime;

@Table(name = "day_operation_time")
@Entity
@NoArgsConstructor
@Getter @Setter
public class DayOperationTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_operation_time_id")
    private Long dayOperationTimeId;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "next_day_linked", nullable = false)
    private Boolean nextDayLinked;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_time_id")
    private OperationTimeEntity operationTimeEntity;

    public static OperationTimeView.DayOperationTime toView(DayOperationTimeEntity dayOperationTimeEntity) {
        return OperationTimeView.DayOperationTime.builder()
                .dayOperationTimeId(dayOperationTimeEntity.getDayOperationTimeId())
                .startTime(dayOperationTimeEntity.getStartTime())
                .endTime(dayOperationTimeEntity.getEndTime())
                .nextDayLinked(dayOperationTimeEntity.getNextDayLinked())
                .build();
    }
}
