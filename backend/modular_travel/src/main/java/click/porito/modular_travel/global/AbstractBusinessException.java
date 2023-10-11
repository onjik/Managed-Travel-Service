package click.porito.modular_travel.global;

/**
 * 모든 도메인에서 발생하는 Business Exception 의 상위 클래스 <br>
 * 여기서 Business Exception 이란 비즈니스 로직 상 발생하는 예외를 의미한다.
 */
public abstract class AbstractBusinessException extends RuntimeException implements ServiceException {

    public abstract String getDomainName();
}
