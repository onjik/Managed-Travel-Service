package click.porito.travel_core.global.response;

public class MessageResponseBody extends AbstractCommonResponseBody {
    protected MessageResponseBody(int status, String message) {
        super(status, message);
    }

    public static MessageResponseBody of(int status, String message) {
        return new MessageResponseBody(status, message);
    }
}
