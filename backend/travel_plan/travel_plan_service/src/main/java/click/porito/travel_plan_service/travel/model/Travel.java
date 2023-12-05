package click.porito.travel_plan_service.travel.model;

import click.porito.travel_plan_service.travel.dto.Plan;
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

    @Column(name = "owner_id")
    private String ownerId;

    @Type(JsonType.class)
    @Column(name = "plan", columnDefinition = "json")
    private Plan plan;
}
