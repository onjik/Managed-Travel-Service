package click.porito.account.account.service;

import click.porito.account.account.*;
import click.porito.account.account.event.AccountDeleteEvent;
import click.porito.account.account.event.AccountPutEvent;
import click.porito.account.account.event.AccountTopics;
import click.porito.account.account.exception.InvalidAuthenticationException;
import click.porito.account.account.model.Account;
import click.porito.account.account.model.Role;
import click.porito.account.account.reposiotry.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class RdbAccountServiceTest {

    AccountService accountService;
    AccountRepository accountRepository;
    static ValidatorFactory validatorFactory;
    Validator validator;
    KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

    @BeforeEach
    void setUp() {
        this.accountRepository = mock(AccountRepository.class);
        this.validator = validatorFactory.getValidator();
        this.kafkaTemplate = mock(KafkaTemplate.class);
        this.accountService = new RdbAccountService(accountRepository, validator, kafkaTemplate);
    }

    private Account createAccount(Long userId) {
        Account account = Account.builder("email@email.com", Role.USER)
                .name("name")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.MALE)
                .build();

        try {
            Field idField = Account.class.getDeclaredField("userId");
            idField.setAccessible(true);
            idField.set(account, userId);

            return account;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    @DisplayName("retrieveAccountById")
    class retrieveAccountById {

        @Test
        @DisplayName("userId가 null 이면 IllegalArgumentException 을 던진다.")
        void retrieveAccountByIdWithNullUserId() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                accountService.retrieveAccountById(null);
            });
        }

        @Test
        @DisplayName("존재하지 않는 userId 를 주면 빈 Optional 을 반환한다.")
        void retrieveAccountByIdWithNonExistentUserId() {
            //given
            Long nonExistentUserId = 123456L;
            when(accountRepository.findByUserId(nonExistentUserId)).thenReturn(Optional.empty());

            //when
            Optional<AccountDTO> result = accountService.retrieveAccountById(nonExistentUserId);

            //then
            Assertions.assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("존재하는 userId 를 주면 해당 AccountDTO 를 반환한다.")
        void retrieveAccountByIdWithExistentUserId() {
            //given
            Long existentUserId = 123456L;
            Account account = createAccount(existentUserId);
            when(accountRepository.findByUserId(existentUserId)).thenReturn(Optional.of(account));

            //when
            Optional<AccountDTO> result = accountService.retrieveAccountById(existentUserId);


            //then
            Assertions.assertTrue(result.isPresent());
            Assertions.assertEquals(account.getUserId(), result.get().getUserId());
        }

    }

    @Nested
    @DisplayName("retrieveCurrentAccount")
    class retrieveCurrentAccount {


        @AfterEach
        void tearDown() {
            SecurityContextHolder.clearContext();
        }

        @Test
        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
        void retrieveCurrentAccountWithNoAccount() {
            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.retrieveCurrentAccount();
            });
        }

        @Test
        @DisplayName("현재 로그인 정보가 있지만, DB에서 조회할 수 없으면 예외를 던진다.")
        void retrieveCurrentAccountWithNoAccountInDB() {
            //given
            Long userId = 123456L;
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.retrieveCurrentAccount();
            });
        }

        @Test
        @DisplayName("현재 로그인한 사용자가 있으면 해당 AccountDTO 를 반환한다.")
        void retrieveCurrentAccountWithAccount() {
            //given
            Long userId = 123456L;
            Account account = createAccount(userId);
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            AccountDTO result = accountService.retrieveCurrentAccount();

            //then
            Assertions.assertEquals(account.getUserId(), result.getUserId());

        }
    }

    @Nested
    @DisplayName("retrieveAccountSummaryById")
    class retrieveAccountSummaryById {
        @Test
        @DisplayName("userId가 null 이면 IllegalArgumentException 을 던진다.")
        void retrieveAccountSummaryByIdWithNullUserId() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                accountService.retrieveAccountSummaryById(null);
            });
        }

        @Test
        @DisplayName("존재하지 않는 userId 를 주면 빈 Optional 을 반환한다.")
        void retrieveAccountSummaryByIdWithNonExistentUserId() {
            //given
            Long nonExistentUserId = 123456L;
            when(accountRepository.findByUserId(nonExistentUserId)).thenReturn(Optional.empty());

            //when
            Optional<AccountSummaryDTO> result = accountService.retrieveAccountSummaryById(nonExistentUserId);

            //then
            Assertions.assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("존재하는 userId 를 주면 해당 AccountSummaryDTO 를 반환한다.")
        void retrieveAccountSummaryByIdWithExistentUserId() {
            //given
            Long existentUserId = 123456L;
            Account account = createAccount(existentUserId);
            when(accountRepository.findByUserId(existentUserId)).thenReturn(Optional.of(account));

            //when
            Optional<AccountSummaryDTO> result = accountService.retrieveAccountSummaryById(existentUserId);
        }


        @Nested
        @DisplayName("getDetailedProfile")
        class getDetailedProfile {

            @Test
            @DisplayName("현재 로그인한 사용자의 상세 프로필을 반환한다.")
            void getCurrentDetailedProfile() {

            }

            @Test
            @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
            void getCurrentDetailedProfileWithNoAccount() {

            }

        }

        @Nested
        @DisplayName("getSimpleProfile")
        class getSimpleProfile {

            @Test
            @DisplayName("현재 로그인한 사용자의 간단한 프로필을 반환한다.")
            void getCurrentSimpleProfile() {

            }
        }

        @Nested
        @DisplayName("deleteAccount")
        class deleteAccount {

            @Test
            @DisplayName("현재 로그인한 사용자의 계정을 삭제한다.")
            void deleteCurrentAccount() {

            }

            @Test
            @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
            void deleteCurrentAccountWithNoAccount() {

            }
        }

        @Nested
        @DisplayName("patchProfileInfo")
        class patchProfileInfo {

            @Test
            @DisplayName("현재 로그인한 사용자의 프로필을 수정한다.")
            void patchCurrentProfileInfo() {

            }

            @Test
            @DisplayName("만약 낙관적 락에 의해 예외가 발생하면, ObjectOptimisticLockingFailureException 던진다.")
            void patchCurrentProfileInfoWithOptimisticLockException() {

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
            @DisplayName("유효한 AccountRegisterDTO 이 주어지면, Account 를 생성한다.")
            void registerAccount() {

            }

            @Test
            @DisplayName("유효하지 않은 AccountRegisterDTO 이 주어지면, ConstraintViolationException 을 던진다.")
            void registerAccountWithInvalidForm() {

            }
        }
    }

    @Nested
    @DisplayName("retrieveCurrentAccountSummary")
    class retrieveCurrentAccountSummary{

        @Test
        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
        void retrieveCurrentAccountSummaryWithNoAccount() {
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.retrieveCurrentAccountSummary();
            });
        }

        @Test
        @DisplayName("현재 로그인 정보가 있지만, DB에서 조회할 수 없으면 예외를 던진다.")
        void retrieveCurrentAccountSummaryWithNoAccountInDB() {
            //given
            Long userId = 123456L;
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.retrieveCurrentAccountSummary();
            });
        }

        @Test
        @DisplayName("현재 로그인한 사용자가 있으면 해당 AccountSummaryDTO 를 반환한다.")
        void retrieveCurrentAccountSummaryWithAccount() {
            //given
            Long userId = 123456L;
            Account account = createAccount(userId);
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            AccountSummaryDTO result = accountService.retrieveCurrentAccountSummary();

            //then
            Assertions.assertEquals(account.getUserId(), result.getUserId());
        }
    }

    @Nested
    @DisplayName("deleteCurrentAccount")
    class deleteCurrentAccount{

        @Test
        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
        void deleteCurrentAccountWithNoAccount() {
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.deleteCurrentAccount();
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("현재 로그인 정보가 있지만, DB에서 조회되지 않으면, 예외를 던진다.")
        void deleteCurrentAccountWithNoAccountInDB() {
            //given
            Long userId = 123456L;
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.deleteCurrentAccount();
            });

            verify(kafkaTemplate, never()).send(anyString(), any());

        }

        @Test
        @DisplayName("현재 로그인 한 사용자가 유효하면, 해당 계정을 삭제하고 SecurityHolder를 비운다. 그리고 계정 삭제 이벤트를 발생시킨다.")
        void deleteCurrentAccount(){
            //given
            Long userId = 123456L;
            Account account = createAccount(userId);
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            accountService.deleteCurrentAccount();

            //then
            verify(accountRepository, times(1)).delete(account);
            Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
            verify(kafkaTemplate, times(1)).send(eq(AccountTopics.ACCOUNT_DELETE_0), any(AccountDeleteEvent.class));
        }
    }

    @Nested
    @DisplayName("patchProfileInfo")
    class patchProfileInfo{

        private AccountPatchDTO createPatchDTO(){
            AccountPatchDTO accountPatchDTO = new AccountPatchDTO();
            accountPatchDTO.setName("changedName");
            return accountPatchDTO;
        }

        @Test
        @DisplayName("인자가 null이면 예외 발생")
        void patchProfileInfoWithNull(){
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                accountService.patchProfileInfo(null);
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("현재 로그인한 사용자가 없으면 예외를 던진다.")
        void patchProfileInfoWithNoAccount() {
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.patchProfileInfo(createPatchDTO());
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("현재 로그인 정보가 있지만, DB에서 조회할 수 없으면 예외를 던진다.")
        void patchProfileInfoWithNoAccountInDB() {
            //given
            Long userId = 123456L;
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            Assertions.assertThrows(InvalidAuthenticationException.class, () -> {
                accountService.patchProfileInfo(new AccountPatchDTO());
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("낙관적 락 충돌이 발생하면, OptimisticLockException 을 던진다.")
        void patchProfileInfoWithOptimisticLockException(){
            //given
            Long userId = 123456L;
            Account account = createAccount(userId);
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));
            when(accountRepository.save(any(Account.class))).thenThrow(ObjectOptimisticLockingFailureException.class);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));

            //when
            Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                accountService.patchProfileInfo(createPatchDTO());
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("인자로 던저준 DTO에 따라 수정이 일어난다. 그리고 이벤트를 발생시킨다.")
        void patchProfileInfo(){
            //given
            Long userId = 123456L;
            Account account = createAccount(userId);
            when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));
            AccountPatchDTO patchDTO = createPatchDTO();
            doAnswer(invocation -> account).when(accountRepository).save(any(Account.class));
            //when
            accountService.patchProfileInfo(patchDTO);

            //then
            //save 인자로 넘겨진 Account 객체의 name 필드가 변경되었는지 확인
            verify(accountRepository, times(1)).save(argThat((Account arg) -> {
                return arg.getName().equals(patchDTO.getName());
            }));
            verify(kafkaTemplate, times(1)).send(eq(AccountTopics.ACCOUNT_PUT_0), any(AccountPutEvent.class));
        }
    }

    @Nested
    @DisplayName("registerAccount")
    class registerAccount{
        private AccountRegisterDTO createValidRegisterDTO(){
            AccountRegisterDTO dto = new AccountRegisterDTO();
            dto.setEmail("email@email.com");
            dto.setName("name");
            dto.setGender(Gender.MALE);
            dto.setBirthDate(LocalDate.of(1999, 1, 1));
            return dto;
        }

        private AccountRegisterDTO createInsufficientRegisterDTO(){
            AccountRegisterDTO dto = new AccountRegisterDTO();
            dto.setEmail("email");
            dto.setName("name");
            return dto;
        }


        @Test
        @DisplayName("Null 인자를 주면 예외를 던진다.")
        void registerAccountWithNull(){
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                accountService.registerAccount(null);
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("유효하지 않은 인자를 주면 예외를 던진다.")
        void registerAccountWithInvalidForm(){
            Assertions.assertThrows(ConstraintViolationException.class, () -> {
                accountService.registerAccount(createInsufficientRegisterDTO());
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("낙관적 락 충돌이 발생하면, OptimisticLockException 을 던진다.")
        void registerAccountWithOptimisticLockException(){
            //given
            when(accountRepository.save(any(Account.class))).thenThrow(ObjectOptimisticLockingFailureException.class);

            //when
            Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                accountService.registerAccount(createValidRegisterDTO());
            });
            verify(kafkaTemplate, never()).send(anyString(), any());
        }

        @Test
        @DisplayName("유효한 인자를 주면, Account 를 생성한다.")
        void registerAccount(){
            //given
            AccountRegisterDTO dto = createValidRegisterDTO();
            Account account = createAccount(1L);
            doReturn(account).when(accountRepository).save(any(Account.class));
            //when
            accountService.registerAccount(dto);

            //then
            //save 인자로 넘겨진 Account 객체의 필드가 인자로 넘겨진 DTO 의 필드와 같은지 확인
            verify(accountRepository, times(1)).save(argThat((Account arg) -> {
                return arg.getEmail().equals(dto.getEmail())
                        && arg.getName().equals(dto.getName())
                        && arg.getGender().equals(dto.getGender())
                        && arg.getBirthDate().equals(dto.getBirthDate());
            }));
            verify(kafkaTemplate, times(1)).send(eq(AccountTopics.ACCOUNT_PUT_0), any(AccountPutEvent.class));

        }
    }
}
