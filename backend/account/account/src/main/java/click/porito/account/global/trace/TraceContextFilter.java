package click.porito.account.global.trace;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static click.porito.account.global.constant.GlobalConstant.CORRELATION_ID_HEADER_NAME;

@Slf4j
@Component
public class TraceContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequestWrapper request = (HttpServletRequestWrapper) servletRequest;
        TraceContext context = TraceContextHolder.getContext();

        String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
        context.setCorrelationId(correlationId);
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        context.setAuthToken(authToken);

        log.debug("correlationId: {}", correlationId);

        try {
            filterChain.doFilter(request, servletResponse);
        } finally {
            TraceContextHolder.clearContext();
        }
    }
}
