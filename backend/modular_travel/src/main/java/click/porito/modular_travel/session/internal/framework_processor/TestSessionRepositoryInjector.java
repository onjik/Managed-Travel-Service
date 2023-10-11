package click.porito.modular_travel.session.internal.framework_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 테스트 환경에서는 레디스를 사용하지 않고, 메모리를 사용하여 테스트 하도록 설정
 */
@Profile("test")
@Component
public class TestSessionRepositoryInjector implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RedisSessionRepository) {
            return new MapSessionRepository(new ConcurrentHashMap<>());
        }
        return bean;
    }
}
