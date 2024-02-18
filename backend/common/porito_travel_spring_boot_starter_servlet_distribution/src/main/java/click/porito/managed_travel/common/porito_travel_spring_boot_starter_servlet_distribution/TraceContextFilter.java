package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_distribution;

import click.porito.common.trace.TraceConstant;
import click.porito.common.trace.TraceContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.io.IOException;


@Slf4j
public class TraceContextFilter implements Filter {

    private final static String CORRELATION_ID_HEADER_NAME = TraceConstant.CORRELATION_HEADER_NAME;




    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequestWrapper request = (HttpServletRequestWrapper) servletRequest;

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
            // clear context after request is processed
            TraceContextHolder.clearContext();
        }
    }
}
