package click.porito.modular_travel.account.internal.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import click.porito.modular_travel.account.internal.dto.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.AccountPrincipal;
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.IS_NEW_ACCOUNT;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.USER_ID;

class AccountServiceTest {
    AccountRepository accountRepository;
    AccountService accountService;

    Long userId;
    AccountPrincipal principal;

    @BeforeEach
    void setup(){
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);

        userId = 1L;
        OidcIdToken token = new OidcIdToken("test", Instant.now(), Instant.now().plusSeconds(60000),
                Map.of(USER_ID, userId,
                        IS_NEW_ACCOUNT, true,
                        "sub", UUID.randomUUID().toString()));
        principal = new AccountPrincipal(new DefaultOidcUser(null, token));
    }

    @Nested
    @DisplayName("getAccountDetailInfo")
    class getAccountDetailsInfo {

        @Test
        @DisplayName("유저 정보가 없는 경우 InvalidAuthenticationException 발생")
        void getAccountDetailInfoWithNoUser(){
            // given
            when(accountRepository.findById(userId)).thenReturn(Optional.empty());

            //when
            Executable executable = () -> accountService.getAccountDetailInfo(principal);

            // then
            assertThrows(InvalidAuthenticationException.class, executable);

        }

        @Test
        @DisplayName("유저 정보가 있는 경우 유저 정보 반환")
        void getAccountDetailInfoWithUser(){
            // given
            Account testUser = Account.builder("test@test.com", Account.Role.ROLE_USER)
                    .name("test")
                    .build();
            when(accountRepository.findById(userId)).thenReturn(Optional.of(testUser));

            // when
            Account result = accountService.getAccountDetailInfo(principal);

            // then
            assertNotNull(result);
            assertEquals(testUser, result);
        }

        @Test
        @DisplayName("인자가 null인 경우, IllegalArgumentException 발생")
        void getAccountDetailInfoWithNull(){
            // given
            // when
            Executable executable = () -> accountService.getAccountDetailInfo(null);

            // then
            assertThrows(IllegalArgumentException.class, executable);
        }
    }

    @Nested
    @DisplayName("getProfileResponse")
    class getProfileResponse {

        @Test
        @DisplayName("유저 정보가 없는 경우 InvalidAuthenticationException 발생")
        void getSimpleProfileWithNoUser(){
            // given
            when(accountRepository.findById(userId)).thenReturn(Optional.empty());

            //when
            Executable executable = () -> accountService.getSimpleProfile(principal);

            // then
            assertThrows(InvalidAuthenticationException.class, executable);

        }

        @Test
        @DisplayName("유저 정보가 있는 경우 간소화된 유저 정보 반환")
        void getSimpleProfileWithUser() {
            // given
            Account testUser = Account.builder("test@test.com", Account.Role.ROLE_USER)
                    .name("test")
                    .build();
            when(accountRepository.findById(userId)).thenReturn(Optional.of(testUser));

            // when
            ProfileResponse result = accountService.getSimpleProfile(principal);

            // then
            assertNotNull(result);
            assertEquals(testUser.getName(), result.name());
        }

        @Test
        @DisplayName("인자가 null인 경우, IllegalArgumentException 발생")
        void getSimpleProfileWithNull(){
            // given
            // when
            Executable executable = () -> accountService.getSimpleProfile(null);

            // then
            assertThrows(IllegalArgumentException.class, executable);
        }
    }

    @Nested
    @DisplayName("deleteAccount")
    class deleteAccount {
        private MockedStatic<SecurityContextHolder> securityContextHolder;

        @BeforeEach
        void beforeAll() {
            securityContextHolder = mockStatic(SecurityContextHolder.class);
        }

        @AfterEach
        void afterAll(){
            securityContextHolder.close();
        }
        @Test
        @DisplayName("유저 정보를 삭제하고, Context를 clear한다.")
        void deleteAccount() {
            // given
            // when
            accountService.deleteAccount(principal);
            //then
            verify(accountRepository, times(1)).deleteById(userId);
            securityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
        }

        @Test
        @DisplayName("만약 유저 정보가 조회되지 않더라도, 정상 작동하고, Context를 clear한다.")
        void deleteAccountWithNoUser() {
            // given
            long invalidLong = userId + 2L;
            when(accountRepository.findById(invalidLong)).thenReturn(Optional.empty());
            // when
            accountService.deleteAccount(principal);
            //then
            verify(accountRepository, times(1)).deleteById(userId);
            securityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
        }

        @Test
        @DisplayName("인자가 null인 경우, IllegalArgumentException 발생")
        void deleteAccountWithNull(){
            // given
            // when
            Executable executable = () -> accountService.deleteAccount(null);

            // then
            assertThrows(IllegalArgumentException.class, executable);
        }
    }


    @Nested
    @DisplayName("patchProfileInfo")
    class patchProfileInfo {

        @Test
        @DisplayName("유저 정보가 없는 경우, InvalidAuthenticationException 발생")
        void patchProfileInfoWithNoUser() {
            // given
            when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
            AccountPatchRequest accountPatchRequest = new AccountPatchRequest();
            // when
            Executable executable = () -> accountService.patchProfileInfo(principal, accountPatchRequest);
            // then
            assertThrows(InvalidAuthenticationException.class, executable);
        }

        @Test
        @DisplayName("유저 정보가 주어진 경우, 기존의 유저 정보를 수정한다.")
        void patchProfileInfoWithUser() {
            // given
            AccountPatchRequest request = new AccountPatchRequest();
            request.setName("new name");

            Account originalUser = Account.builder("test@test.com", Account.Role.ROLE_USER)
                    .name("test")
                    .build();
            when(accountRepository.findById(userId)).thenReturn(Optional.of(originalUser));

            // when
            accountService.patchProfileInfo(principal, request);

            // then
            verify(accountRepository, times(1)).save(
                    argThat(user -> {
                        return user.getName().equals(request.getName()) &&
                                user.getEmail().equals(originalUser.getEmail());
                    }));

        }

        @Test
        @DisplayName("인자가 하나라도 null인 경우 IllegalArgumentException 발생")
        void patchProfileInfoWithNull() {
            // given
            AccountPatchRequest accountPatchRequest = new AccountPatchRequest();
            // when
            Executable executable1 = () -> accountService.patchProfileInfo(null, accountPatchRequest);
            Executable executable2 = () -> accountService.patchProfileInfo(principal, null);
            Executable executable3 = () -> accountService.patchProfileInfo(null, null);

            // then
            assertThrows(IllegalArgumentException.class, executable1);
            assertThrows(IllegalArgumentException.class, executable2);
            assertThrows(IllegalArgumentException.class, executable3);
        }
    }
}