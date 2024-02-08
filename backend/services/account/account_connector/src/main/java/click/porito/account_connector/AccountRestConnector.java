package click.porito.account_connector;

import click.porito.account_common.api.AccountApi;
import click.porito.account_common.api.request.AccountInfoPatchRequest;
import click.porito.account_common.api.request.AccountRegisterRequest;
import click.porito.account_common.api.response.AccountSummaryResponse;
import click.porito.account_common.domain.Account;
import click.porito.account_common.exception.AccountBusinessException;
import click.porito.account_common.exception.AccountServerException;
import click.porito.common.exception.Domain;
import click.porito.connector.AbstractRestConnector;
import click.porito.connector.RestExchangeable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

@Slf4j
public class AccountRestConnector extends AbstractRestConnector implements AccountApi {


    public AccountRestConnector(RestExchangeable restExchangeable, String uriPrefix) {
        super(restExchangeable, uriPrefix);
    }

    @Override
    public Account retrieveAccountById(String userId) throws AccountBusinessException, AccountServerException {
        return doExchange("/accounts/{userId}", HttpMethod.GET, null, Account.class, userId).orElse(null);
    }

    @Override
    public AccountSummaryResponse retrieveAccountSummaryById(String userId) throws AccountBusinessException, AccountServerException {
        return doExchange("/accounts/{userId}/summary", HttpMethod.GET, null, AccountSummaryResponse.class, userId).orElse(null);
    }

    @Override
    public void patchProfileInfo(String userId, @Valid AccountInfoPatchRequest body) throws AccountBusinessException, AccountServerException {
        doRequest("/accounts/{userId}", HttpMethod.PATCH, new HttpEntity<>(body), Void.class, userId);
    }

    @Override
    public Account registerAccount(@Valid AccountRegisterRequest dto) throws AccountBusinessException, AccountServerException {
        return doExchange("/accounts", HttpMethod.POST, new HttpEntity<>(dto), Account.class).orElse(null);
    }

    @Override
    public void deleteAccount(String userId) throws AccountBusinessException, AccountServerException {
        doRequest("/accounts/{userId}", HttpMethod.DELETE, null, Void.class, userId);
    }

    @NonNull
    @Override
    protected Domain getDomain() {
        return Domain.ACCOUNT;
    }
}
