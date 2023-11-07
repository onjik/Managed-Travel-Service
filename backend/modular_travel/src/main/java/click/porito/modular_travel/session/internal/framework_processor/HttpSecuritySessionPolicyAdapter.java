package click.porito.modular_travel.session.internal.framework_processor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpSecuritySessionPolicyAdapter implements BeanPostProcessor {

    @Value("${spring.session.max-sessions:3}")
    private int maxSessions;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof HttpSecurity http){
            adaptSessionPolicy(http);
        }
        return bean;
    }

    private void adaptSessionPolicy(HttpSecurity http){
        try {
            http.sessionManagement(session -> {session
                    .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
                    .invalidSessionStrategy(new HttpStatusInvalidSessionStrategy(eventPublisher))
                    .sessionConcurrency(sessionConcurrency -> sessionConcurrency
                            .maximumSessions(maxSessions)
                            .maxSessionsPreventsLogin(false)
                            .expiredSessionStrategy(new UnauthorizedSessionExpiredStrategy(eventPublisher))
                    )
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
            });
        } catch (Exception e) {
            throw new BeanCreationException("Failed to adapt global security", e);
        }
    }

    /**
     * 세션이 만료되었을 경우, 401 Unauthorized 를 반환하는 전략
     * Body 에는 json 으로 { "message": "Session is expired" } 를 반환한다.
     */
    @Slf4j
    static class HttpStatusInvalidSessionStrategy implements InvalidSessionStrategy {

        private final static String BODY = "{\"message\": \"Session is Invalid\"}";
        private final ApplicationEventPublisher eventPublisher;

        HttpStatusInvalidSessionStrategy(ApplicationEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
        }

        @Override
        public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
            log.debug("Invalid session detected");
            responseUnauthorized(response);
            replaceSession(request);
            publishEvent(request);
        }

        void responseUnauthorized(HttpServletResponse response) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(BODY);
            writer.flush();
        }

        void replaceSession(HttpServletRequest request){
            SecurityContextHolder.clearContext();
            request.getSession(true);
        }

        void publishEvent(HttpServletRequest request){
            //TODO: 이벤트 발생
        }
    }

    @RequiredArgsConstructor
    static class UnauthorizedSessionExpiredStrategy implements SessionInformationExpiredStrategy {

        private final static String BODY = "{\"message\": \"Session is expired\"}";

        private final ApplicationEventPublisher eventPublisher;

        @Override
        public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
            responseUnauthorized(event.getResponse());
            publishEvent(event.getSessionInformation());
        }

        void responseUnauthorized(HttpServletResponse response) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(BODY);
            writer.flush();
        }

        void publishEvent(SessionInformation information){
            //TODO: 이벤트 발생

        }
    }
}
