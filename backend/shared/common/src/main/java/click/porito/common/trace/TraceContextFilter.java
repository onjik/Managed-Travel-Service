package click.porito.common.trace;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

import static click.porito.common.trace.TraceConstant.CORRELATION_ID_HEADER_NAME;

@Slf4j
public class TraceContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequestWrapper request = (HttpServletRequestWrapper) servletRequest;
        HttpServletResponseWrapper response = (HttpServletResponseWrapper) servletResponse;

        // parse correlationId from incoming request
        String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
        if (correlationId != null) {
            TraceContext context = TraceContextHolder.getContext();
            context.setCorrelationId(correlationId);
            log.debug("Parsed correlationId from incoming request: {}", correlationId);
        }
        // parse jwtToken from incoming request
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            TraceContext context = TraceContextHolder.getContext();
            context.setJwtToken(jwtToken);
            log.debug("Parsed jwtToken from incoming request: {}", jwtToken);
        }

        try {
            filterChain.doFilter(request, servletResponse);
        } finally {
            // add correlationId to response header
            TraceContextHolder.getContext().getCorrelationId()
                    .ifPresent(correlationIdValue -> response.addHeader(CORRELATION_ID_HEADER_NAME, correlationIdValue));
            // clear context after request is processed
            TraceContextHolder.clearContext();
        }
    }
}
