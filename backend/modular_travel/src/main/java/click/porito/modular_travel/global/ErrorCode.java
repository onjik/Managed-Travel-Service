package click.porito.modular_travel.global;

import org.springframework.lang.Nullable;

public interface ErrorCode {
    String getDomainName();
    String getErrorCode();
    @Nullable
    String getDescription();
}
