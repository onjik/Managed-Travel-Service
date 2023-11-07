package click.porito.modular_travel.account;

import java.util.Optional;

public interface AccountInternalApi extends AccountExternalApi {
    Optional<AccountDTO> getAccountByEmail(String email);
}
