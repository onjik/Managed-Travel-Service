package click.porito.modular_travel.account.internal.service;

import click.porito.modular_travel.account.internal.dto.AccountRegisterForm;
import click.porito.modular_travel.account.internal.dto.view.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.view.DetailedProfile;
import click.porito.modular_travel.account.internal.dto.view.SimpleProfile;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;
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

/**
 * Account (여기서는 특별히, 현재 로그인 한 사용자를 가르킴) 에 대한 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final Validator validator;

    /**
     * 현재 로그인 한 사용자의 Account 를 가져옴
     * @return 현재 로그인 한 사용자의 Account
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 없을 때 발생
     */
    @NonNull
    private Account getCurrentAccount() throws InvalidAuthenticationException{
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        long id = Long.parseLong(name);
        return accountRepository.findById(id).orElseThrow(InvalidAuthenticationException::new);
    }

    @Override
    public DetailedProfile getDetailedProfile() {
        Account account = getCurrentAccount();
        return DetailedProfile.from(account);
    }

    @Override
    public SimpleProfile getSimpleProfile() {
        Account account = getCurrentAccount();
        return SimpleProfile.from(account);
    }

    @Override
    public void deleteAccount() {
        Account account = getCurrentAccount();
        accountRepository.delete(account);
        //clear authentication
        SecurityContextHolder.clearContext();
    }

    /**
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     */
    @Override
    public void patchProfileInfo(AccountPatchRequest body) throws ObjectOptimisticLockingFailureException {
        Account account = getCurrentAccount();
        LocalDate birthDate = body.getBirthDate();
        if (birthDate != null) {
            account.setBirthDate(birthDate);
        }
        Account.Gender gender = body.getGender();
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
    public Account registerAccount(@Valid AccountRegisterForm dto) throws ConstraintViolationException {
        Account createdAccount = Account.builder(dto.getEmail(), Account.Role.USER)
                .name(dto.getName())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .build();

        return accountRepository.save(createdAccount);
    }
}
