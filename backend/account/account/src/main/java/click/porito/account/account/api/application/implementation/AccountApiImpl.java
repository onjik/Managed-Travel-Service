package click.porito.account.account.api.application.implementation;

import click.porito.account.account.api.application.AccountApi;
import click.porito.account.account.api.request.AccountInfoPatchRequest;
import click.porito.account.account.api.request.AccountRegisterRequest;
import click.porito.account.account.api.response.AccountSummaryResponse;
import click.porito.account.account.domain.Account;
import click.porito.account.account.exception.UserNotFoundException;
import click.porito.account.account.operation.AccountOperation;
import click.porito.account.security.operation.JwtOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * AccountEntity (여기서는 특별히, 현재 로그인 한 사용자를 가르킴) 에 대한 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class AccountApiImpl implements AccountApi {
    private final AccountOperation accountOperation;
    private final JwtOperation jwtOperation;


    @Override
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public Account retrieveAccountById(String userId) throws UserNotFoundException {
        Assert.notNull(userId, "userId must not be null");
        return accountOperation.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public AccountSummaryResponse retrieveAccountSummaryById(String userId) throws UserNotFoundException {
        Assert.notNull(userId, "userId must not be null");
        return accountOperation.findByUserId(userId)
                .map(account -> {
                    return AccountSummaryResponse.builder()
                            .userId(account.getUserId())
                            .name(account.getName())
                            .profileImgUri(account.getProfileImgUri())
                            .build();
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @PreAuthorize("hasPermission(#userId, 'click.porito.account.account.domain.Account', 'UPDATE')")
    public void patchProfileInfo(String userId, @Valid AccountInfoPatchRequest body) throws UserNotFoundException {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(body, "body must not be null");
        Account account = accountOperation.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
        if (body.name() != null) {
            account.setName(body.name());
        }
        if (body.gender() != null) {
            account.setGender(body.gender());
        }
        if (body.birthDate() != null) {
            account.setBirthDate(body.birthDate());
        }

        accountOperation.update(account);
    }

    @Override
    @PreAuthorize("hasAuthority('account:create:new') || hasAuthority('account:create')")
    public Account registerAccount(@Valid AccountRegisterRequest body) throws UserNotFoundException {
        Assert.notNull(body, "body must not be null");
        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return accountOperation.create(roles,body);
    }

    @Override
    @PreAuthorize("hasPermission(#userId, 'click.porito.account.account.domain.Account', 'DELETE')")
    public void deleteAccount(String userId) throws UserNotFoundException {
        Assert.notNull(userId, "userId must not be null");
        accountOperation.deleteByUserId(userId);
    }

}
