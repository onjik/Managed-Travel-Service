package click.porito.modular_travel.gateway.router;

import click.porito.modular_travel.account.AccountExternalApi;
import click.porito.modular_travel.account.AccountSummaryDTO;
import click.porito.modular_travel.gateway.exception.ResponseEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRouter {
    private final AccountExternalApi accountService;

    @GetMapping("/users/{userId}/profile")
    public AccountSummaryDTO getProfile(@PathVariable("userId") Long userId) {
        return accountService.getAccountSummaryByUserId(userId)
                .orElseThrow(ResponseEntityNotFoundException::new);
    }
}
