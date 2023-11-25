package click.porito.modular_travel.account.service;

import click.porito.modular_travel.account.*;
import click.porito.modular_travel.account.event.AccountDeleteEvent;
import click.porito.modular_travel.account.event.AccountPutEvent;
import click.porito.modular_travel.account.event.AccountTopics;
import click.porito.modular_travel.account.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.model.Account;
import click.porito.modular_travel.account.model.Role;
import click.porito.modular_travel.account.reposiotry.AccountRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * Account (여기서는 특별히, 현재 로그인 한 사용자를 가르킴) 에 대한 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class RdbAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final Validator validator;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new InvalidAuthenticationException();
        }
        String name = authentication.getName();
        try {
            return Long.parseLong(name);
        } catch (NumberFormatException e) {
            throw new InvalidAuthenticationException();
        }
    }

    @Override
    public Optional<AccountDTO> retrieveAccountById(Long userId) {
        Assert.notNull(userId, "userId must not be null");
        return accountRepository.findByUserId(userId)
                .map(AccountDTO.class::cast);
    }

    /**
     * 현재 로그인 한 사용자의 Account 를 가져옴
     * @return 현재 로그인 한 사용자의 Account
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 없을 때 발생
     */
    @NonNull
    @Override
    public AccountDTO retrieveCurrentAccount() throws InvalidAuthenticationException{
        long id = getCurrentUserId();
        return accountRepository.findByUserId(id)
                .map(AccountDTO.class::cast)
                .orElseThrow(InvalidAuthenticationException::new);
    }

    @Override
    public Optional<AccountSummaryDTO> retrieveAccountSummaryById(Long userId) {
        Assert.notNull(userId, "userId must not be null");
        return accountRepository.findByUserId(userId)
                .map(AccountSummaryDTO.class::cast);
    }

    @Override
    public AccountSummaryDTO retrieveCurrentAccountSummary() {
        long id = getCurrentUserId();
        return accountRepository.findByUserId(id)
                .map(AccountSummaryDTO.class::cast)
                .orElseThrow(InvalidAuthenticationException::new);
    }

    @Override
    public Optional<AccountDTO> retrieveAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(AccountDTO.class::cast);
    }

    @Transactional
    @Override
    public void deleteCurrentAccount() {
        Long id = getCurrentUserId();
        Account account = accountRepository.findByUserId(id)
                .orElseThrow(InvalidAuthenticationException::new);
        accountRepository.delete(account);
        //kafka event publish
        var event = new AccountDeleteEvent(account.getUserId().toString());
        //clear authentication
        SecurityContextHolder.clearContext();

        kafkaTemplate.send(AccountTopics.ACCOUNT_DELETE_0, event);
    }

    /**
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     */
    @Override
    public void patchProfileInfo(AccountPatchDTO body) throws ObjectOptimisticLockingFailureException {
        Assert.notNull(body, "body must not be null");
        Long id = getCurrentUserId();
        Account account = accountRepository.findByUserId(id)
                .orElseThrow(InvalidAuthenticationException::new);
        LocalDate birthDate = body.getBirthDate();
        if (birthDate != null) {
            account.setBirthDate(birthDate);
        }
        Gender gender = body.getGender();
        if (gender != null){
            account.setGender(gender);
        }
        String name = body.getName();
        if (name != null) {
            account.setName(name);
        }
        Account saved = accountRepository.save(account);
        //kafka event publish
        AccountPutEvent event = AccountPutEvent.from(saved);
        kafkaTemplate.send(AccountTopics.ACCOUNT_PUT_0, event);

    }

    /**
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     * @throws ConstraintViolationException 인자의 유효성 검사 실패
     */
    @Transactional
    @Override
    public Account registerAccount(AccountRegisterDTO dto) throws ConstraintViolationException {
        Assert.notNull(dto, "dto must not be null");
        //validation
        Set<ConstraintViolation<AccountRegisterDTO>> validate = validator.validate(dto);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
        //create account
        Account createdAccount = Account.builder(dto.getEmail(), Role.USER)
                .name(dto.getName())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .build();
        Account saved = accountRepository.save(createdAccount);
        //kafka event publish
        AccountPutEvent event = AccountPutEvent.from(saved);
        kafkaTemplate.send(AccountTopics.ACCOUNT_PUT_0, event);
        //return
        return saved;
    }



}
