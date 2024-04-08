package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import com.vladmihalcea.hibernate.type.array.DoubleArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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

    @Type(DoubleArrayType.class)
    @Column(name = "embedding", columnDefinition = "double precision[]")
    private double[] embedding;

}
