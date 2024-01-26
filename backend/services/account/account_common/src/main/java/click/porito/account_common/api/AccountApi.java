package click.porito.account_common.api;

import click.porito.account_common.api.request.AccountInfoPatchRequest;
import click.porito.account_common.api.request.AccountRegisterRequest;
import click.porito.account_common.api.response.AccountSummaryResponse;
import click.porito.account_common.domain.Account;
import click.porito.account_common.exception.UserNotFoundException;
import jakarta.validation.Valid;

/**
 * 현재 로그인한 AccountEntity 에 대한 서비스
 */
public interface AccountApi {
    Account retrieveAccountById(String userId) throws UserNotFoundException;
    AccountSummaryResponse retrieveAccountSummaryById(String userId) throws UserNotFoundException;

    /**
     * 현재 로그인 한 사용자의 프로필을 수정함
     * @param body 수정할 프로필
     * @throws UserNotFoundException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void patchProfileInfo(String userId, @Valid AccountInfoPatchRequest body) throws UserNotFoundException;

    Account registerAccount(@Valid AccountRegisterRequest dto) throws UserNotFoundException;

    void deleteAccount(String userId) throws UserNotFoundException;
}
