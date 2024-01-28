package click.porito.account_common.api;

import click.porito.account_common.api.request.AccountInfoPatchRequest;
import click.porito.account_common.api.request.AccountRegisterRequest;
import click.porito.account_common.api.response.AccountSummaryResponse;
import click.porito.account_common.domain.Account;
import click.porito.account_common.exception.UserNotFoundException;
import click.porito.common.exception.ServerThrownException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * 현재 로그인한 AccountEntity 에 대한 서비스
 */
@Validated
public interface AccountApi {
    Account retrieveAccountById(String userId) throws ServerThrownException;
    AccountSummaryResponse retrieveAccountSummaryById(String userId) throws ServerThrownException;

    /**
     * 현재 로그인 한 사용자의 프로필을 수정함
     * @param body 수정할 프로필
     * @throws UserNotFoundException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void patchProfileInfo(String userId, @Valid AccountInfoPatchRequest body) throws ServerThrownException;

    Account registerAccount(@Valid AccountRegisterRequest dto) throws ServerThrownException;

    void deleteAccount(String userId) throws ServerThrownException;
}
