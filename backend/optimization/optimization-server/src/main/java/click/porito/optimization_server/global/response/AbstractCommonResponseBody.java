package click.porito.optimization_server.global.response;

import lombok.Getter;

@Getter
public abstract class AbstractCommonResponseBody {
    protected int status;
    protected String message;

    public AbstractCommonResponseBody(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
