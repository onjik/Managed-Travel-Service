package click.porito.modular_travel.account.internal.service;

import click.porito.modular_travel.account.internal.dto.AccountRegisterForm;
import click.porito.modular_travel.account.internal.dto.view.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.view.DetailedProfile;
import click.porito.modular_travel.account.internal.dto.view.SimpleProfile;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * 현재 로그인한 Account 에 대한 서비스
 */
public interface AccountService {
    /**
     * 현재 로그인 한 사용자의 상세 프로필을 가져옴
     * @return 현재 로그인 한 사용자의 상세 프로필
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    DetailedProfile getDetailedProfile() throws InvalidAuthenticationException;

    /**
     * 현재 로그인 한 사용자의 간단한 프로필을 가져옴
     * @return 현재 로그인 한 사용자의 간단한 프로필
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    SimpleProfile getSimpleProfile()  throws InvalidAuthenticationException;

    /**
     * 현재 로그인 한 사용자의 계정을 삭제함
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void deleteAccount()  throws InvalidAuthenticationException;

    /**
     * 현재 로그인 한 사용자의 프로필을 수정함
     * @param body 수정할 프로필
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     * @throws InvalidAuthenticationException 현재 로그인 한 사용자가 조회되지 않을 때 발생
     */
    void patchProfileInfo(AccountPatchRequest body) throws ObjectOptimisticLockingFailureException ,InvalidAuthenticationException;

    /**
     * @throws ObjectOptimisticLockingFailureException : 동시에 같은 계정에 대한 수정이 일어났을 때 발생
     */
    Account registerAccount(@Valid AccountRegisterForm dto) throws ConstraintViolationException;
}
