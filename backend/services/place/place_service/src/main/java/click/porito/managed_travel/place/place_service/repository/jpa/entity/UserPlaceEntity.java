package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_place")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserPlaceEntity extends PlaceEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountSnapshotEntity accountSnapshotEntity;
}
