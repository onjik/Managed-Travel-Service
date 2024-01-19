package click.porito.travel_core.global.response;

public class DataResponseBody<T> extends AbstractCommonResponseBody {
    private final T data;

    protected DataResponseBody(int status, String message, T data) {
        super(status, message);
        this.data = data;
    }

    public static <T> DataResponseBody<T> of(int status, String message, T data) {
        return new DataResponseBody<>(status, message, data);
    }
}
