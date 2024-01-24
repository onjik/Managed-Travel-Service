package click.porito.account.account.api.application;

import click.porito.account.account.api.request.AccountInfoPatchRequest;
import click.porito.account.account.api.request.AccountRegisterRequest;
import click.porito.account.account.api.response.AccountSummaryResponse;
import click.porito.account.account.domain.Account;
import click.porito.account.account.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * 현재 로그인한 AccountEntity 에 대한 서비스
 */
public interface AccountApi {
    Account retrieveAccountById(String userId) throws UserNotFoundException;
    AccountSummaryResponse retrieveAccountSummaryById(String userId) throws UserNotFoundException;

    /**
     * 현재 로그인 한 사용자의 프로필을 수정함
     * @param body 수정할 프로필
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     * @throws UserNotFoundException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void patchProfileInfo(String userId, @Valid AccountInfoPatchRequest body) throws UserNotFoundException;

    Account registerAccount(@Valid AccountRegisterRequest dto) throws UserNotFoundException;

    void deleteAccount(String userId) throws UserNotFoundException;
}
