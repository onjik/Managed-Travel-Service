package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Table(name = "place_article")
@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"placeArticleId", "title"})
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
    @JoinColumn(name = "user_id")
    private UserPrincipal userPrincipal;

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

    @Version
    @Column(name = "version")
    private Long version;
}
