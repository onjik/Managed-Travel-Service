package click.porito.account.account.operation;

import click.porito.account.account.api.request.AccountRegisterRequest;
import click.porito.account.account.domain.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;

public interface AccountOperation {
    Optional<Account> findByUserId(String userId);

    Optional<Account> findByEmail(String email);

    void deleteByUserId(String userId);

    void update(Account account);

    Account create(List<GrantedAuthority> roles, AccountRegisterRequest request);

}
