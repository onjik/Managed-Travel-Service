package click.porito.modular_travel.session.internal.framework_processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 레디스가 사용되는 환경에서, 애플리케이션 시작시 레디스의 연결이 정상적으로 이루어졌는지 확인하는 클래스
 */
@Slf4j
@Profile({"local", "prod", "alpha"})
@Component
public class RedisConnectionChecker implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        RedisSessionRepository sessionRepository = applicationContext.getBean(RedisSessionRepository.class);
        Assert.notNull(sessionRepository, "RedisSessionRepository must not be null");
        //레디스 연결이 잘되는지 ping을 보내서 확인한다.
        try {
             sessionRepository.getSessionRedisOperations().execute((RedisCallback<Object>) RedisConnectionCommands::ping);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed", e);
            throw new IllegalStateException("Redis connection failed", e);
        }
    }
}
