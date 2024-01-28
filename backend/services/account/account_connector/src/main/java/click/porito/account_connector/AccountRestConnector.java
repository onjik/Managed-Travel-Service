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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class AccountRestConnector extends AbstractRestConnector implements AccountApi {

    public AccountRestConnector(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public Account retrieveAccountById(String userId) throws AccountBusinessException, AccountServerException {
        return doExchange(
                restTemplate -> restTemplate.getForEntity("/accounts/{userId}", Account.class, userId)
        ).orElse(null);
    }

    @Override
    public AccountSummaryResponse retrieveAccountSummaryById(String userId) throws AccountBusinessException, AccountServerException {
        return doExchange(
                restTemplate -> restTemplate.getForEntity("/accounts/{userId}/summary", AccountSummaryResponse.class, userId)
        ).orElse(null);
    }

    @Override
    public void patchProfileInfo(String userId, @Valid AccountInfoPatchRequest body) throws AccountBusinessException, AccountServerException {
        doRequest(
                restTemplate1 ->
                    restTemplate1.exchange(
                            "/accounts/{userId}", HttpMethod.PATCH,
                            new HttpEntity<>(body), Void.class,
                            userId)
        );
    }

    @Override
    public Account registerAccount(@Valid AccountRegisterRequest dto) throws AccountBusinessException, AccountServerException {
        return doExchange(
                restTemplate1 -> restTemplate1.postForEntity("/account", dto, Account.class)
        ).orElse(null);
    }

    @Override
    public void deleteAccount(String userId) throws AccountBusinessException, AccountServerException {
        doRequest(
                restTemplate1 -> restTemplate1.exchange("/accounts/{userId}", HttpMethod.DELETE, null, Void.class, userId)
        );
    }

    @NonNull
    @Override
    protected Domain getDomain() {
        return Domain.ACCOUNT;
    }
}
