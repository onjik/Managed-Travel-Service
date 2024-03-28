package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.Instant;
import java.util.List;

@Table(name = "review",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "place_user_unique",
                columnNames = {"place_id", "user_id"})
        }
)
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ReviewEntity {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @Range(min = 1, max = 10)
    @Column(name = "rate")
    private Integer rate; // 1 - 10
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private PlaceEntity placeEntity;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserPrincipal userPrincipal;
    @Version
    @Column(name = "version")
    private Long version;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "review_place_media",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<PlaceMediaEntity> placeMediaList;
}
