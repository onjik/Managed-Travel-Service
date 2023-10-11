package click.porito.modular_travel.account.internal.exception;

import org.springframework.security.core.Authentication;

/**
 * 로그인이 완료된 시점에서, 현재 컨텍스트의 {@link Authentication} 객체에 담겨진 정보가 유효하지 않을 경우 발생하는 예외<br>
 * 이 예외를 처리하는 측에서는, 인증 정보를 무효화 시키는 책임을 가진다.
 */
public class InvalidAuthenticationException extends AccountBusinessException{

    public InvalidAuthenticationException() {
    }
}
