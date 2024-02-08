package click.porito.common.trace;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;


/**
 * RestTemplate 에서 보내는 요청에 correlationId 와 authToken 을 추가하는 인터셉터
 */
public class TraceContextInterceptor implements ClientHttpRequestInterceptor {

    private final String CORRELATION_ID_HEADER_NAME;

    public TraceContextInterceptor(TraceConfigurationProperties traceConfigurationProperties) {
        this.CORRELATION_ID_HEADER_NAME = traceConfigurationProperties.headerName();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        // correlationId 와 authToken 을 추가한다.
        if (TraceContextHolder.hasContext()) {
            TraceContext context = TraceContextHolder.getContext();
            context.getCorrelationId()
                    .ifPresent(c -> headers.add(CORRELATION_ID_HEADER_NAME, c));
            context.getJwtToken()
                    .ifPresent(j -> headers.add(HttpHeaders.AUTHORIZATION, j));
        }
        return execution.execute(request, body);
    }
}
