package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Table(name = "review",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "place_user_unique",
                columnNames = {"place_id", "account_id"})
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
    @Column(name = "rate", nullable = false)
    private Integer rate; // 1 - 10
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private OfficialPlaceEntity placeEntity;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountSnapshotEntity accountSnapshotEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "review_place_media",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<PlaceMediaEntity> placeMediaList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.getReviewId() == null) return false;
        ReviewEntity that = (ReviewEntity) o;
        if (that.getReviewId() == null) return false;
        return Objects.equals(getReviewId(), that.getReviewId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReviewId());
    }
}
