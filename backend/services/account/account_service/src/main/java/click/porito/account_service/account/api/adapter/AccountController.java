package click.porito.account_service.account.api.adapter;

import click.porito.managed_travel.domain.api.AccountApi;
import click.porito.managed_travel.domain.api.request.AccountInfoPatchRequest;
import click.porito.managed_travel.domain.api.request.AccountRegisterRequest;
import click.porito.managed_travel.domain.api.response.AccountSummaryResponse;
import click.porito.managed_travel.domain.domain.Account;
import click.porito.managed_travel.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountApi accountApi;

    @GetMapping("/accounts/{accountId}")
    public Account retrieveAccountById(@PathVariable("accountId") String accountId) {
        return accountApi.retrieveAccountById(accountId);
    }

    @GetMapping("/accounts/{accountId}/summary")
    public AccountSummaryResponse retrieve(
            @PathVariable("accountId") String accountId
    ) {
        return accountApi.retrieveAccountSummaryById(accountId);
    }

    @PatchMapping("/accounts/{accountId}")
    public ResponseEntity<Void> patchProfileInfo(
            @PathVariable("accountId") String accountId,
            @RequestBody AccountInfoPatchRequest body) {
        accountApi.patchProfileInfo(accountId, body);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable("accountId") String accountId
    ) {
        accountApi.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accounts/{accountId}/profile")
    public AccountSummaryResponse getProfile(@PathVariable("accountId") String accountId) {
        return accountApi.retrieveAccountSummaryById(accountId);
    }

    @PostMapping("/account")
    public Account registerAccount(AccountRegisterRequest dto) throws UserNotFoundException {
        return accountApi.registerAccount(dto);
    }

}
