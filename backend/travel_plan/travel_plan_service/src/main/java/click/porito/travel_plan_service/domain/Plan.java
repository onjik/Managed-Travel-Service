package click.porito.travel_plan_service.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Document(collection = "plan")
public class Plan {
    @Id
    private String planId;
    private String title;
    private LocalDate startDate;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    @Version
    private Long version;
    private String ownerId;
    private List<RouteComponent> route;
}
