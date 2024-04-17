package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "official_place")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class OfficialPlaceEntity extends PlaceEntity {
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "google_place_id", unique = true)
    private String googlePlaceId;

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<ReviewEntity> reviews;

}
