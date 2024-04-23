package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.*;

@Table(name = "place")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "name")
    private String name;

    @Column(name = "keywords")
    @Type(StringArrayType.class)
    private List<String> keywords = new ArrayList<>();

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
    @Column(name = "google_place_id")
    private String googlePlaceId;
    @Column(name = "is_public")
    private Boolean isPublic;
    @Column(name = "is_official")
    private Boolean isOfficial;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "place_category",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<PlaceArticleEntity> placeArticles;



    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<OperationTimeEntity> operationTimes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountSnapshotEntity publisher;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.getPlaceId() == null) return false;
        PlaceEntity that = (PlaceEntity) o;
        if (that.getPlaceId() == null) return false;
        return Objects.equals(getPlaceId(), that.getPlaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlaceId());
    }
}
