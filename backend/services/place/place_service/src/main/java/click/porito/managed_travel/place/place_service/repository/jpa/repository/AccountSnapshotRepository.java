package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.place_service.repository.jpa.entity.AccountSnapshotEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSnapshotRepository extends org.springframework.data.repository.Repository<AccountSnapshotEntity, Long> {
    Optional<AccountSnapshotEntity> findAccountSnapshotEntitiesByAccountId(Long accountId);

}
