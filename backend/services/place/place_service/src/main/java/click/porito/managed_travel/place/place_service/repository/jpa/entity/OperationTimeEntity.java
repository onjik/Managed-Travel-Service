package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import click.porito.managed_travel.place.domain.view.OperationTimeView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "operation_time")
@Entity
@Data
@NoArgsConstructor
public class OperationTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_time_id")
    private Long operationTimeId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private PlaceEntity placeEntity;

    @OneToMany(mappedBy = "operationTimeEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DayOperationTimeEntity> dayOperationTimeEntities = new ArrayList<>();

    public static OperationTimeView toView(OperationTimeEntity operationTimeEntity) {
        Assert.notNull(operationTimeEntity, "operationTimeEntity must not be null");
        List<OperationTimeView.DayOperationTime> dayOperationTimes = operationTimeEntity.getDayOperationTimeEntities()
                .stream()
                .map(DayOperationTimeEntity::toView)
                .toList();
        return OperationTimeView.builder()
                .operationTimeId(operationTimeEntity.getOperationTimeId())
                .startDate(operationTimeEntity.getStartDate())
                .endDate(operationTimeEntity.getEndDate())
                .dayOperationTimes(dayOperationTimes)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.getOperationTimeId() == null) return false;
        OperationTimeEntity that = (OperationTimeEntity) o;
        return Objects.equals(getOperationTimeId(), that.getOperationTimeId()) && Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getEndDate(), that.getEndDate()) && Objects.equals(getPlaceEntity(), that.getPlaceEntity()) && Objects.equals(getDayOperationTimeEntities(), that.getDayOperationTimeEntities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperationTimeId(), getStartDate(), getEndDate(), getPlaceEntity(), getDayOperationTimeEntities());
    }
}
