package click.porito.optimization_server.global.response;

public class MessageResponseBody extends AbstractCommonResponseBody {
    protected MessageResponseBody(int status, String message) {
        super(status, message);
    }

    public static MessageResponseBody of(int status, String message) {
        return new MessageResponseBody(status, message);
    }
}
