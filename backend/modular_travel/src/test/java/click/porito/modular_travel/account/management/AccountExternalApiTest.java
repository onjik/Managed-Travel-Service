//package click.porito.modular_travel.account.internal.service;
//
//import click.porito.modular_travel.account.Gender;
//import click.porito.modular_travel.account.model.Role;
//import click.porito.modular_travel.account.AccountRegisterDTO;
//import click.porito.modular_travel.account.AccountPatchDTO;
//import click.porito.modular_travel.account.internal.dto.view.DetailedProfile;
//import click.porito.modular_travel.account.internal.dto.view.AccountSummaryDTO;
//import click.porito.modular_travel.account.model.Account;
//import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
//import click.porito.modular_travel.account.reposiotry.AccountRepository;
//import click.porito.modular_travel.account.AccountExternalApi;
//import click.porito.modular_travel.account.management.AccountManagement;
//import jakarta.validation.ConstraintViolationException;
//import jakarta.validation.Validator;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.function.Executable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.orm.ObjectOptimisticLockingFailureException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//class AccountExternalApiTest {
//
//    AccountRepository accountRepository;
//    Validator validator;
//    AccountExternalApi accountExternalApi;
//    Authentication authentication;
//    Account account;
//    @BeforeEach
//    void setUp() {
//        accountRepository = spy(AccountRepository.class);
//        validator = mock(Validator.class);
//        accountExternalApi = new AccountManagement(accountRepository, validator);
//        authentication = mock(Authentication.class);
//        when(authentication.getName()).thenReturn("1");
//        account = Account.builder("test@email.com", Role.USER)
//                .build();
//
//
//        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
//        emptyContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(emptyContext);
//    }
//
//    @AfterEach
//    void tearDown() {
//        SecurityContextHolder.clearContext();
//    }
//
//
//    @Nested
//    @DisplayName("getDetailedProfile")
//    class getDetailedProfile {
//
//        @Test
//        @DisplayName("현재 로그인한 사용자의 상세 프로필을 반환한다.")
//        void getCurrentDetailedProfile() {
//            //given
//            Account account = Account.builder("test@email.com", Role.USER)
//                    .build();
//            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
//
//            //when
//            DetailedProfile detailedProfile = accountExternalApi.getDetailedProfile();
//
//            //then
//            Assertions.assertEquals(detailedProfile.getName(), account.getName());
//        }
//
//        @Test
//        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
//        void getCurrentDetailedProfileWithNoAccount() {
//            //given
//            when(accountRepository.findById(1L)).thenReturn(Optional.empty());
//
//            //when
//            Assertions.assertThrows(InvalidAuthenticationException.class, () -> accountExternalApi.getDetailedProfile());
//        }
//
//    }
//
//    @Nested
//    @DisplayName("getSimpleProfile")
//    class getSimpleProfile {
//
//        @Test
//        @DisplayName("현재 로그인한 사용자의 간단한 프로필을 반환한다.")
//        void getCurrentSimpleProfile() {
//            //given
//            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
//
//            //when
//            AccountSummaryDTO simpleProfile = accountExternalApi.getSimpleProfile();
//
//            //then
//            Assertions.assertEquals(simpleProfile.getName(), account.getName());
//
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteAccount")
//    class deleteAccount {
//
//        @Test
//        @DisplayName("현재 로그인한 사용자의 계정을 삭제한다.")
//        void deleteCurrentAccount() {
//            //given
//            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
//
//            //when
//            accountExternalApi.deleteCurrentAccount();
//
//            //then
//            verify(accountRepository, times(1)).delete(account);
//            Assertions.assertEquals(accountRepository.count(), 0);
//        }
//
//        @Test
//        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
//        void deleteCurrentAccountWithNoAccount() {
//            //given
//            when(accountRepository.findById(1L)).thenReturn(Optional.empty());
//
//            //when
//            Assertions.assertThrows(InvalidAuthenticationException.class, () -> accountExternalApi.deleteCurrentAccount());
//        }
//    }
//
//    @Nested
//    @DisplayName("patchProfileInfo")
//    class patchProfileInfo{
//
//        @Test
//        @DisplayName("현재 로그인한 사용자의 프로필을 수정한다.")
//        void patchCurrentProfileInfo() {
//            //given
//            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
//
//            //when
//            AccountPatchDTO accountPatchRequest = new AccountPatchDTO();
//            accountPatchRequest.setBirthDate(LocalDate.of(1999, 1, 1));
//            accountPatchRequest.setName("test");
//            accountExternalApi.patchProfileInfo(accountPatchRequest);
//
//            //then
//            Assertions.assertEquals(account.getBirthDate(), LocalDate.of(1999, 1, 1));
//            Assertions.assertEquals(account.getName(), "test");
//            verify(accountRepository, times(1)).save(account);
//        }
//
//        @Test
//        @DisplayName("만약 낙관적 락에 의해 예외가 발생하면, ObjectOptimisticLockingFailureException 던진다.")
//        void patchCurrentProfileInfoWithOptimisticLockException() {
//            //given
//            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
//            doThrow(new ObjectOptimisticLockingFailureException("dd",new RuntimeException())).when(accountRepository).save(account);
//
//            //when
//            AccountPatchDTO accountPatchRequest = new AccountPatchDTO();
//            accountPatchRequest.setBirthDate(LocalDate.of(1999, 1, 1));
//            accountPatchRequest.setName("test");
//            Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> accountExternalApi.patchProfileInfo(accountPatchRequest));
//        }
//    }
//
//    @Nested
//    @Transactional
//    @ActiveProfiles("test")
//    @DisplayName("registerAccount")
//    @SpringBootTest
//    class registerAccount {
//
//        @Autowired
//        AccountExternalApi injectedAccountExternalApi;
//
//        @Test
//        @DisplayName("유효한 AccountRegisterDTO 이 주어지면, Account 를 생성한다.")
//        void registerAccount() {
//            //given
//            when(accountRepository.save(any())).thenReturn(account);
//
//            var form = AccountRegisterDTO.builder()
//                    .birthDate(LocalDate.of(1999, 1, 1))
//                    .email("emai@email.com")
//                    .gender(Gender.MALE)
//                    .name("test")
//                    .build();
//
//            //when
//            Account createdAccount = accountExternalApi.registerAccount(form);
//
//            //then
//            Assertions.assertEquals(createdAccount, account);
//        }
//
//        @Test
//        @DisplayName("유효하지 않은 AccountRegisterDTO 이 주어지면, ConstraintViolationException 을 던진다.")
//        void registerAccountWithInvalidForm() {
//            //given
//            when(accountRepository.save(any())).thenReturn(account);
//
//            var form = AccountRegisterDTO.builder()
//                    .birthDate(LocalDate.of(1999, 1, 1))
//                    .build();
//
//            //when
//            Executable executable = () -> injectedAccountExternalApi.registerAccount(form);
//
//            //then
//            Assertions.assertThrows(ConstraintViolationException.class, executable);
//        }
//    }
//}