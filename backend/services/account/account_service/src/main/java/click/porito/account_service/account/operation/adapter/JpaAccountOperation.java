package click.porito.account_service.account.operation.adapter;

import click.porito.account_service.account.operation.AccountOperation;
import click.porito.account_service.account.operation.adapter.persistence.entity.AccountEntity;
import click.porito.account_service.account.operation.adapter.persistence.entity.GenderProperty;
import click.porito.account_service.account.operation.adapter.persistence.reposiotry.AccountRepository;
import click.porito.common.util.Mapper;
import click.porito.managed_travel.domain.api.request.AccountInfoPatchRequest;
import click.porito.managed_travel.domain.api.request.AccountRegisterRequest;
import click.porito.managed_travel.domain.domain.Account;
import click.porito.managed_travel.domain.domain.Gender;
import click.porito.managed_travel.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaAccountOperation implements AccountOperation {
    private final AccountRepository accountRepository;
    private final Mapper<AccountEntity, Account> toDomainMapper;

    @Override
    public Optional<Account> findByUserId(String userId) {
        Long userIdLong = Long.parseLong(userId);
        return accountRepository.findByUserId(userIdLong)
                .filter(accountEntity -> accountEntity.getDeletedAt() == null)
                .map(toDomainMapper::map);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .filter(accountEntity -> accountEntity.getDeletedAt() == null)
                .map(toDomainMapper::map);
    }

    @Override
    public void deleteByUserId(String userId) {
        Long userIdLong = Long.parseLong(userId);
        AccountEntity fetchedEntity = accountRepository.findById(userIdLong)
                .orElseThrow(UserNotFoundException::new);
        fetchedEntity.setDeletedAt(Instant.now());
        accountRepository.save(fetchedEntity);
    }

    @Override
    public void update(String userId, AccountInfoPatchRequest request) {
        Long userIdLong = Long.parseLong(userId);
        AccountEntity fetchedEntity = accountRepository.findById(userIdLong)
                .orElseThrow(UserNotFoundException::new);

        fetchedEntity.setName(request.name());
        fetchedEntity.setBirthDate(request.birthDate());
        Gender gender = request.gender();
        GenderProperty genderProperty = switch (gender) {
            case MALE -> GenderProperty.MALE;
            case FEMALE -> GenderProperty.FEMALE;
            default -> null;
        };
        fetchedEntity.setGender(genderProperty);

        accountRepository.save(fetchedEntity);
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
