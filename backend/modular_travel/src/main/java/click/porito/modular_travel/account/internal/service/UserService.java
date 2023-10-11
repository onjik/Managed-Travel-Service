package click.porito.modular_travel.account.internal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.UserNotFoundException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;

    public ProfileResponse getProfile(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return ProfileResponse.from(account);
    }
}
