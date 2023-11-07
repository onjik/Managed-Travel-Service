package click.porito.modular_travel.account;

import click.porito.modular_travel.account.exception.InvalidAuthenticationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

/**
 * 현재 로그인한 Account 에 대한 서비스
 */
public interface AccountExternalApi {
    Optional<AccountDTO> getAccountByUserId(Long userId);
    AccountDTO getCurrentAccount();
    Optional<AccountSummaryDTO> getAccountSummaryByUserId(Long userId);
    AccountSummaryDTO getCurrentAccountSummary();

    /**
     * 현재 로그인 한 사용자의 계정을 삭제함
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void deleteCurrentAccount()  throws InvalidAuthenticationException;

    /**
     * 현재 로그인 한 사용자의 프로필을 수정함
     * @param body 수정할 프로필
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void patchProfileInfo(AccountPatchDTO body) throws ObjectOptimisticLockingFailureException ,InvalidAuthenticationException;


    /**
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     */
    AccountDTO registerAccount(@Valid AccountRegisterDTO dto) throws ConstraintViolationException;
}
