package click.porito.managed_travel.place.place_service.util;

import click.porito.managed_travel.place.place_service.repository.jpa.entity.AccountSnapshotEntity;

import java.util.List;
import java.util.Optional;

public interface AccountContextStrategy {
    Optional<Long> getAccountId();
    Optional<AccountSnapshotEntity> getAccountSnapshot();

    List<String> getAuthorities();

    boolean hasAuthority(String authority);
}
