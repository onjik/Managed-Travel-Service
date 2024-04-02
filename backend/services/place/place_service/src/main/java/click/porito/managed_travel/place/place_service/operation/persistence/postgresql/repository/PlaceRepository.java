package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.repository;

import click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
}
