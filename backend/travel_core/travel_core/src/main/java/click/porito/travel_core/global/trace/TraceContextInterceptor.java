package click.porito.travel_core.global.trace;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static click.porito.travel_core.global.constant.GlobalConstant.CORRELATION_ID_HEADER_NAME;

public class TraceContextInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        TraceContext context = TraceContextHolder.getContext();
        headers.add(CORRELATION_ID_HEADER_NAME, context.getCorrelationId());
        headers.add(HttpHeaders.AUTHORIZATION, context.getAuthToken());
        return execution.execute(request, body);
    }
}
