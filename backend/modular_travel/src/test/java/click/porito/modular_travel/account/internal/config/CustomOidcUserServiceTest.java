package click.porito.modular_travel.account.internal.config;

import click.porito.modular_travel.account.internal.dto.AccountRegisterForm;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.authentication.OidcEmailNotVerifiedException;
import click.porito.modular_travel.account.internal.exception.authentication.OidcInsufficientUserInfoException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;
import click.porito.modular_travel.account.internal.service.AccountService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CustomOidcUserServiceTest {
    AccountService accountService;
    CustomOidcUserService customOidcUserService;
    AccountRepository accountRepository;
    Validator validator;
    OidcUser oidcUser;
    OidcUserRequest oidcUserRequest;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        accountRepository = mock(AccountRepository.class);
        validator = mock(Validator.class);
        customOidcUserService = spy(new CustomOidcUserService(accountService, accountRepository, validator));

        oidcUserRequest = mock(OidcUserRequest.class);
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("clientId")
                .redirectUri("redirectUri")
                .authorizationUri("authorizationUri")
                .tokenUri("tokenUri")
                .build();

        when(oidcUserRequest.getClientRegistration()).thenReturn(clientRegistration);
        when(oidcUserRequest.getIdToken()).thenReturn(mock(OidcIdToken.class));

        //inner mock
        oidcUser = mock(OidcUser.class);
        OidcUserService oidcUserService = mock(OidcUserService.class);
        when(oidcUserService.loadUser(any())).thenReturn(oidcUser);
        customOidcUserService.setOidcUserService(oidcUserService);
    }

    @Nested
    @DisplayName("loadUser")
    class loadUserTest {


        @Test
        @DisplayName("email verified 가 false 이거나 null 인 경우, OidcEmailNotVerifiedException 발생")
        void emailVerifiedFalse() {
            //given
            when(oidcUser.getEmailVerified()).thenReturn(false);

            //when
            Executable executable = () -> customOidcUserService.loadUser(oidcUserRequest);
            //then
            assertThrows(OidcEmailNotVerifiedException.class, executable);
        }

        @Test
        @DisplayName("email 이 null 인 경우, OidcEmailNotVerifiedException 발생")
        void emailNull() {
            //given
            when(oidcUser.getEmail()).thenReturn(null);

            //when
            Executable executable = () -> customOidcUserService.loadUser(oidcUserRequest);

            //then
            assertThrows(OidcEmailNotVerifiedException.class, executable);
        }

        @Test
        @DisplayName("신규 유저이면서 필수 정보가 부족한 경우, OidcInsufficientUserInfoException 발생")
        void newAccountAndInsufficientUserInfo() {
            //given
            doReturn(true).when(customOidcUserService).isEmailVerified(any());
            doReturn(false).when(customOidcUserService).isRegisteredUser(any());
            doReturn(mock(AccountRegisterForm.class)).when(customOidcUserService).createRegisterForm(any());
            Set mock = mock(Set.class);
            when(mock.isEmpty()).thenReturn(false);
            when(validator.validate(any())).thenReturn(mock);

            //when
            Executable executable = () -> customOidcUserService.loadUser(oidcUserRequest);

            //then
            assertThrows(OidcInsufficientUserInfoException.class, executable);
        }

        @Test
        @DisplayName("신규 유저이면서 필수 정보가 부족하지 않은 경우, registerAccount 호출 후 OidcUser 반환")
        void newAccountAndSufficientUserInfo() {
            //given
            doReturn(true).when(customOidcUserService).isEmailVerified(any());
            doReturn(false).when(customOidcUserService).isRegisteredUser(any());
            doReturn(mock(AccountRegisterForm.class)).when(customOidcUserService).createRegisterForm(any());
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            Account account = mock(Account.class);
            OidcUser mockedOidcUser = mock(OidcUser.class);
            when(account.toOidcUser(any())).thenReturn(mock(OidcUser.class));
            when(accountService.registerAccount(any())).thenReturn(account);

            //when
            OidcUser loaded = customOidcUserService.loadUser(oidcUserRequest);

            //then
            assertNotNull(loaded);
            verify(account, times(1)).toOidcUser(any());
            verify(accountService, times(1)).registerAccount(any());

        }

        @Test
        @DisplayName("기존 유저인 경우, accountRepository.findByEmail 호출 후 OidcUser 반환")
        void existingAccount() {
            //given
            doReturn(true).when(customOidcUserService).isEmailVerified(any());
            doReturn(true).when(customOidcUserService).isRegisteredUser(any());
            Account account = mock(Account.class);
            when(account.toOidcUser(any())).thenReturn(mock(OidcUser.class));
            when(accountRepository.findByEmail(any())).thenReturn(java.util.Optional.of(account));

            //when
            OidcUser loaded = customOidcUserService.loadUser(oidcUserRequest);

            //then
            assertNotNull(loaded);
            verify(account, times(1)).toOidcUser(any());
            verify(accountRepository, times(1)).findByEmail(any());
        }
    }




}