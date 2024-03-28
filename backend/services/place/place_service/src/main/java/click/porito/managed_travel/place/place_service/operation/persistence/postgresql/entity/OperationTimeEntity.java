package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Table(name = "operation_time")
@Entity
public class OperationTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_time_id")
    private Long operationTimeId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "place_id")
    private PlaceEntity placeEntity;

    @OneToMany(mappedBy = "operationTimeEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DayOperationTime> dayOperationTimeEntities;

    @Version
    @Column(name = "version")
    private Long version;
}
