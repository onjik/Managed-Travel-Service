package click.porito.modular_travel.account.reposiotry;

import click.porito.modular_travel.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Account> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}
