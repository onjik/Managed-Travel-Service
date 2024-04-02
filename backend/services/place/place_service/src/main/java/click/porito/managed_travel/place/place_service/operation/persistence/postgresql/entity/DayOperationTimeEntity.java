package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "next_day_linked")
    private Boolean nextDayLinked;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operation_time_id")
    private OperationTimeEntity operationTimeEntity;

    @Version
    @Column(name = "version")
    private Long version;
}
