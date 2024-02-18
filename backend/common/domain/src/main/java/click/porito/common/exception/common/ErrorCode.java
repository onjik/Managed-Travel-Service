package click.porito.common.exception.common;


public interface ErrorCode {
    int getStatus();
    String getCode();
    String getDebugDescription();
}
