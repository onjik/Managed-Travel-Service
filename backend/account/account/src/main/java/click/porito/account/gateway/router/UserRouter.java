package click.porito.account.gateway.router;

import click.porito.account.account.AccountService;
import click.porito.account.account.AccountSummaryDTO;
import click.porito.account.gateway.exception.ResponseEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRouter {
    private final AccountService accountService;

    @GetMapping("/users/{userId}/profile")
    public AccountSummaryDTO getProfile(@PathVariable("userId") Long userId) {
        return accountService.retrieveAccountSummaryById(userId)
                .orElseThrow(ResponseEntityNotFoundException::new);
    }
}
