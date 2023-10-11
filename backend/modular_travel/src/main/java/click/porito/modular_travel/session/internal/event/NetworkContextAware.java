package click.porito.modular_travel.session.internal.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;

import java.util.Optional;

public interface NetworkContextAware {

    Context getNetworkEventContext();

    public class Context {
        private String method;
        private String uri;
        private String queryString;
        private String remoteUser;
        private String remoteAddr;
        private String remoteHost;
        private Integer remotePort;
        private String sessionId;


        @Builder
        public Context(String method, String uri, String queryString, String remoteUser, String remoteAddr, String remoteHost, int remotePort, String sessionId) {
            this.method = method;
            this.uri = uri;
            this.queryString = queryString;
            this.remoteUser = remoteUser;
            this.remoteAddr = remoteAddr;
            this.remoteHost = remoteHost;
            this.remotePort = remotePort;
            this.sessionId = sessionId;
        }

        public static Context from(HttpServletRequest request){
            return Context.builder()
                    .method(request.getMethod())
                    .uri(request.getRequestURI())
                    .queryString(request.getQueryString())
                    .remoteUser(request.getRemoteUser())
                    .remoteAddr(request.getRemoteAddr())
                    .remoteHost(request.getRemoteHost())
                    .remotePort(request.getRemotePort())
                    .sessionId(
                            Optional.ofNullable(request.getSession(false))
                                    .map(HttpSession::getId)
                                    .orElse(null))
                    .build();
        }
    }
}
