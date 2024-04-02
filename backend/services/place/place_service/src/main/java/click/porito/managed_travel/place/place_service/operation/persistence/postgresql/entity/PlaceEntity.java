package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.time.Instant;
import java.util.List;

@Table(name = "place")
@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"placeId", "name"})
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "name")
    private String name;

    @Column(name = "keywords")
    @Type(StringArrayType.class)
    private List<String> keywords;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website")
    private String website;
    @Column(name = "summary")
    private String summary;
    @Column(name = "location")
    private Point location;
    @Column(name = "boundary")
    private Polygon boundary;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "google_place_id")
    private String googlePlaceId;
    @Column(name = "is_public")
    private Boolean isPublic;
    @Version
    @Column(name = "version")
    private Long version;
    @ManyToMany
    @JoinTable(
            name = "place_category",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categories;

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<PlaceArticleEntity> placeArticles;

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<OperationTimeEntity> operationTimes;

}
