package click.porito.travel_core.plan.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
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
    private List<RouteComponent> route = new ArrayList<>();

    @Builder
    public Plan(String title, LocalDate startDate, String ownerId, List<RouteComponent> route) {
        this.title = title;
        this.startDate = startDate;
        this.ownerId = ownerId;
        this.route = route;
    }
}
