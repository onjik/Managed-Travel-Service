package click.porito.account_service.account.operation.adapter;

import click.porito.managed_travel.domain.api.request.AccountRegisterRequest;
import click.porito.managed_travel.domain.domain.Account;
import click.porito.managed_travel.domain.exception.UserNotFoundException;
import click.porito.account_service.account.operation.AccountOperation;
import click.porito.account_service.account.operation.adapter.persistence.entity.AccountEntity;
import click.porito.account_service.account.operation.adapter.persistence.entity.GenderProperty;
import click.porito.account_service.account.operation.adapter.persistence.reposiotry.AccountRepository;
import click.porito.common.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaAccountOperation implements AccountOperation {
    private final AccountRepository accountRepository;
    private final Mapper<Account, AccountEntity> toEntityMapper;
    private final Mapper<AccountEntity, Account> toDomainMapper;

    @Override
    public Optional<Account> findByUserId(String userId) {
        Long userIdLong = Long.parseLong(userId);
        return accountRepository.findByUserId(userIdLong)
                .map(toDomainMapper::map);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(toDomainMapper::map);
    }

    @Override
    public void deleteByUserId(String userId) {
        Long userIdLong = Long.parseLong(userId);
        accountRepository.deleteByUserId(userIdLong);
    }

    @Override
    public void update(Account account) {
        Assert.notNull(account, "account must not be null");
        Assert.notNull(account.getUserId(), "account.userId() must not be null");
        Long userId = Long.parseLong(account.getUserId());
        AccountEntity accountEntity = toEntityMapper.map(account);
        AccountEntity fetchedEntity = accountRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        /*
        JPA Repository 의 save 메서드는
        버전 필드가 있을 경우, 버전 == null 로 새로운 insert와 update를 구분한다.
         */
        accountEntity.setVersion(fetchedEntity.getVersion());

        accountRepository.save(accountEntity);
    }

    @Override
    public Account create(List<GrantedAuthority> roles, AccountRegisterRequest request) {
        String name = request.name();
        String email = request.email();
        GenderProperty genderProperty;
        if (request.gender() != null) {
            genderProperty = GenderProperty.valueOf(
                    request.gender().name()
            );
        } else {
            genderProperty = null;
        }
        LocalDate birthDate = request.birthDate();
        var roleProperties = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        AccountEntity accountEntity = AccountEntity.builder(email, roleProperties)
                .name(name)
                .gender(genderProperty)
                .birthDate(birthDate)
                .build();

        AccountEntity saved = accountRepository.save(accountEntity);
        return toDomainMapper.map(saved);
    }
}
