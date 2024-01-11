package click.porito.account.account.reposiotry;

import click.porito.account.account.model.Account;
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
