package click.porito.modular_travel.account.internal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import click.porito.modular_travel.account.internal.dto.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.AccountPrincipal;
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;

/**
 * Account (여기서는 특별히, 현재 로그인 한 사용자를 가르킴) 에 대한 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account getAccountDetailInfo(AccountPrincipal principal) {
        Assert.notNull(principal, "principal must not be null");
        Long userId = principal.getUserId();
        log.debug("getAccountDetailInfo: userId={}", userId);
        return accountRepository.findById(userId).orElseThrow(() -> new InvalidAuthenticationException());
    }

    public ProfileResponse getSimpleProfile(AccountPrincipal principal) {
        Assert.notNull(principal, "principal must not be null");
        Long userId = principal.getUserId();
        log.debug("getSimpleProfile: userId={}", userId);
        return accountRepository.findById(userId)
                .map(ProfileResponse::from)
                .orElseThrow(InvalidAuthenticationException::new);
    }


    public void deleteAccount(AccountPrincipal principal) {
        Assert.notNull(principal, "principal must not be null");
        Long userId = principal.getUserId();
        log.debug("deleteAccount: userId={}", userId);
        accountRepository.deleteById(userId);
        SecurityContextHolder.clearContext();
    }

    public void patchProfileInfo(AccountPrincipal principal, AccountPatchRequest body) {
        Assert.notNull(principal, "principal must not be null");
        Assert.notNull(body, "body must not be null");
        Long userId = principal.getUserId();
        log.debug("patchProfileInfo: userId={}", userId);
        Account account = accountRepository.findById(userId).orElseThrow(InvalidAuthenticationException::new);
        if (body.getName() != null) {
            account.setName(body.getName());
        }
        if (body.getGender() != null) {
            account.setGender(body.getGender());
        }
        if (body.getBirthDate() != null) {
            account.setBirthDate(body.getBirthDate());
        }
        accountRepository.save(account);
    }
}
