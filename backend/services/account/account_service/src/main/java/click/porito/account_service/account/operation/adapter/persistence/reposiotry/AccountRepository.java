package click.porito.account_service.account.operation.adapter.persistence.reposiotry;

import click.porito.account_service.account.operation.adapter.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<AccountEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}
