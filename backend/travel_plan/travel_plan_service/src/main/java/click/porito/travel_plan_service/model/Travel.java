package click.porito.travel_plan_service.model;

import click.porito.travel_plan_service.dto.Plan;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;

@Entity(name = "travel")
public class Travel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Type(JsonType.class)
    @Column(name = "plan", columnDefinition = "json")
    private Plan plan;
}
