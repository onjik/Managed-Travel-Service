package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Table(name = "account_snapshot")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class AccountSnapshotEntity {
    /*
    NEVER BE DELETED
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "snapshot_timestamp", nullable = false)
    private Instant snapshotTimestamp;

    @Column(name = "deleted_at", nullable = false)
    private Instant deletedAt;

    @Column(name = "is_temp", nullable = false)
    private Boolean isTemp;

    @OneToMany(mappedBy = "accountSnapshotEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserPlaceEntity> userPlaceEntities;

    @OneToMany(mappedBy = "accountSnapshotEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlaceMediaEntity> placeMediaEntities;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "place_article_like",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "place_article_id")
    )
    private Set<PlaceArticleEntity> likedPlaceArticleEntities;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "place_like",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private Set<OfficialPlaceEntity> likedPlaceEntities;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "review_like",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    private Set<ReviewEntity> likedReviewEntities;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "place_media_like",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<PlaceMediaEntity> likedPlaceMediaEntities;

}
