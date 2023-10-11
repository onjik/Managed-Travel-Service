package click.porito.modular_travel.account.internal.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.UserNotFoundException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    AccountRepository accountRepository;
    UserService userService;

    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        userService = new UserService(accountRepository);
    }

    @Nested
    @DisplayName("getProfile")
    class getProfile {

        @Test
        @DisplayName("유저가 존재하지 않을 경우 UserNotFoundException 발생")
        void getProfile() {
            //given
            long userId = 1L;
            when(accountRepository.findById(userId)).thenReturn(Optional.empty());
            //when
            Executable executable = () -> userService.getProfile(userId);
            //then
            assertThrows(UserNotFoundException.class, executable);
        }

        @Test
        @DisplayName("유저가 존재할 경우 ProfileResponse 반환")
        void getProfile2() {
            //given
            long userId = 1L;
            String name = "test";
            Account user = Account.builder("test@email.com", Account.Role.ROLE_USER)
                    .name(name)
                    .gender(Account.Gender.MALE)
                    .build();
            when(accountRepository.findById(userId)).thenReturn(Optional.of(user));
            //when
            ProfileResponse response = userService.getProfile(userId);
            //then
            assertNotNull(response);
            assertEquals(name, response.name());
        }
    }

}