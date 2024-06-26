package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private PlaceEntity placeEntity;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountSnapshotEntity accountSnapshotEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "review_media_reference",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<MediaReferenceEntity> placeMediaList = new HashSet<>();

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
