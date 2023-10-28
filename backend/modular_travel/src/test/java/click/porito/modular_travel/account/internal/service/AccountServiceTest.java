package click.porito.modular_travel.account.internal.service;

import click.porito.modular_travel.account.internal.dto.AccountRegisterForm;
import click.porito.modular_travel.account.internal.dto.view.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.view.DetailedProfile;
import click.porito.modular_travel.account.internal.dto.view.SimpleProfile;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class AccountServiceTest {

    AccountRepository accountRepository;
    Validator validator;
    AccountService accountService;
    Authentication authentication;
    Account account;
    @BeforeEach
    void setUp() {
        accountRepository = spy(AccountRepository.class);
        validator = mock(Validator.class);
        accountService = new AccountServiceImpl(accountRepository, validator);
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("1");
        account = Account.builder("test@email.com", Account.Role.USER)
                .build();


        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(emptyContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Nested
    @DisplayName("getDetailedProfile")
    class getDetailedProfile {

        @Test
        @DisplayName("현재 로그인한 사용자의 상세 프로필을 반환한다.")
        void getCurrentDetailedProfile() {
            //given
            Account account = Account.builder("test@email.com", Account.Role.USER)
                    .build();
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

            //when
            DetailedProfile detailedProfile = accountService.getDetailedProfile();

            //then
            Assertions.assertEquals(detailedProfile.getName(), account.getName());
        }

        @Test
        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
        void getCurrentDetailedProfileWithNoAccount() {
            //given
            when(accountRepository.findById(1L)).thenReturn(Optional.empty());

            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> accountService.getDetailedProfile());
        }

    }

    @Nested
    @DisplayName("getSimpleProfile")
    class getSimpleProfile {

        @Test
        @DisplayName("현재 로그인한 사용자의 간단한 프로필을 반환한다.")
        void getCurrentSimpleProfile() {
            //given
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

            //when
            SimpleProfile simpleProfile = accountService.getSimpleProfile();

            //then
            Assertions.assertEquals(simpleProfile.getName(), account.getName());

        }
    }

    @Nested
    @DisplayName("deleteAccount")
    class deleteAccount {

        @Test
        @DisplayName("현재 로그인한 사용자의 계정을 삭제한다.")
        void deleteCurrentAccount() {
            //given
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

            //when
            accountService.deleteAccount();

            //then
            verify(accountRepository, times(1)).delete(account);
            Assertions.assertEquals(accountRepository.count(), 0);
        }

        @Test
        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
        void deleteCurrentAccountWithNoAccount() {
            //given
            when(accountRepository.findById(1L)).thenReturn(Optional.empty());

            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> accountService.deleteAccount());
        }
    }

    @Nested
    @DisplayName("patchProfileInfo")
    class patchProfileInfo{

        @Test
        @DisplayName("현재 로그인한 사용자의 프로필을 수정한다.")
        void patchCurrentProfileInfo() {
            //given
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

            //when
            AccountPatchRequest accountPatchRequest = new AccountPatchRequest();
            accountPatchRequest.setBirthDate(LocalDate.of(1999, 1, 1));
            accountPatchRequest.setName("test");
            accountService.patchProfileInfo(accountPatchRequest);

            //then
            Assertions.assertEquals(account.getBirthDate(), LocalDate.of(1999, 1, 1));
            Assertions.assertEquals(account.getName(), "test");
            verify(accountRepository, times(1)).save(account);
        }

        @Test
        @DisplayName("만약 낙관적 락에 의해 예외가 발생하면, ObjectOptimisticLockingFailureException 던진다.")
        void patchCurrentProfileInfoWithOptimisticLockException() {
            //given
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
            doThrow(new ObjectOptimisticLockingFailureException("dd",new RuntimeException())).when(accountRepository).save(account);

            //when
            AccountPatchRequest accountPatchRequest = new AccountPatchRequest();
            accountPatchRequest.setBirthDate(LocalDate.of(1999, 1, 1));
            accountPatchRequest.setName("test");
            Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> accountService.patchProfileInfo(accountPatchRequest));
        }
    }

    @Nested
    @Transactional
    @ActiveProfiles("test")
    @DisplayName("registerAccount")
    @SpringBootTest
    class registerAccount {

        @Autowired
        AccountService injectedAccountService;

        @Test
        @DisplayName("유효한 AccountRegisterForm 이 주어지면, Account 를 생성한다.")
        void registerAccount() {
            //given
            when(accountRepository.save(any())).thenReturn(account);

            var form = AccountRegisterForm.builder()
                    .birthDate(LocalDate.of(1999, 1, 1))
                    .email("emai@email.com")
                    .gender(Account.Gender.MALE)
                    .name("test")
                    .build();

            //when
            Account createdAccount = accountService.registerAccount(form);

            //then
            Assertions.assertEquals(createdAccount, account);
        }

        @Test
        @DisplayName("유효하지 않은 AccountRegisterForm 이 주어지면, ConstraintViolationException 을 던진다.")
        void registerAccountWithInvalidForm() {
            //given
            when(accountRepository.save(any())).thenReturn(account);

            var form = AccountRegisterForm.builder()
                    .birthDate(LocalDate.of(1999, 1, 1))
                    .build();

            //when
            Executable executable = () -> injectedAccountService.registerAccount(form);

            //then
            Assertions.assertThrows(ConstraintViolationException.class, executable);
        }
    }
}