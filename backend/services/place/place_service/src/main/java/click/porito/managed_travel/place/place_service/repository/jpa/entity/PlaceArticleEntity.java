package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Table(name = "place_article")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class PlaceArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_article_id")
    private Long placeArticleId;

    @Column(name = "title", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountSnapshotEntity accountSnapshotEntity;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private PlaceEntity placeEntity;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "is_temp")
    private Boolean isTemp;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.getPlaceArticleId() == null) return false;
        PlaceArticleEntity that = (PlaceArticleEntity) o;
        if (that.getPlaceArticleId() == null) return false;
        return Objects.equals(getPlaceArticleId(), that.getPlaceArticleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlaceArticleId());
    }
}
