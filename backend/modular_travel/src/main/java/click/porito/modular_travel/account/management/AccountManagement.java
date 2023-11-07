package click.porito.modular_travel.account.management;

import click.porito.modular_travel.account.*;
import click.porito.modular_travel.account.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.model.Account;
import click.porito.modular_travel.account.Gender;
import click.porito.modular_travel.account.model.Role;
import click.porito.modular_travel.account.reposiotry.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Account (여기서는 특별히, 현재 로그인 한 사용자를 가르킴) 에 대한 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class AccountManagement implements AccountExternalApi, AccountInternalApi {
    private final AccountRepository accountRepository;
    private final Validator validator;

    private Long getCurrentUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(name);
    }

    @Override
    public Optional<AccountDTO> getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .map(AccountDTO.class::cast);
    }

    /**
     * 현재 로그인 한 사용자의 Account 를 가져옴
     * @return 현재 로그인 한 사용자의 Account
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 없을 때 발생
     */
    @NonNull
    @Override
    public AccountDTO getCurrentAccount() throws InvalidAuthenticationException{
        long id = getCurrentUserId();
        return accountRepository.findByUserId(id)
                .map(AccountDTO.class::cast)
                .orElseThrow(InvalidAuthenticationException::new);
    }

    @Override
    public Optional<AccountSummaryDTO> getAccountSummaryByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .map(AccountSummaryDTO.class::cast);
    }

    @Override
    public AccountSummaryDTO getCurrentAccountSummary() {
        long id = getCurrentUserId();
        return accountRepository.findByUserId(id)
                .map(AccountSummaryDTO.class::cast)
                .orElseThrow(InvalidAuthenticationException::new);
    }

    @Override
    public void deleteCurrentAccount() {
        Long id = getCurrentUserId();
        accountRepository.deleteByUserId(id);
        //clear authentication
        SecurityContextHolder.clearContext();
    }

    /**
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     */
    @Override
    public void patchProfileInfo(AccountPatchDTO body) throws ObjectOptimisticLockingFailureException {
        Long id = getCurrentUserId();
        Account account = accountRepository.findByUserId(id)
                .orElseThrow(InvalidAuthenticationException::new);
        LocalDate birthDate = body.getBirthDate();
        if (birthDate != null) {
            account.setBirthDate(birthDate);
        }
        Gender gender = body.getGender();
        if (gender != null){
            account.setGender(gender);
        }
        String name = body.getName();
        if (name != null) {
            account.setName(name);
        }
        accountRepository.save(account);
    }

    /**
     * @throws ConstraintViolationException 인자의 유효성 검사 실패
     */
    @Override
    public Account registerAccount(@Valid AccountRegisterDTO dto) throws ConstraintViolationException {
        Account createdAccount = Account.builder(dto.getEmail(), Role.USER)
                .name(dto.getName())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .build();
        return accountRepository.save(createdAccount);
    }


    @Override
    public Optional<AccountDTO> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(AccountDTO.class::cast);
    }
}
