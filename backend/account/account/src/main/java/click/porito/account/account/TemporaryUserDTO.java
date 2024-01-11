package click.porito.account.account;

import java.time.OffsetDateTime;

public interface TemporaryUserDTO {
    String getEmail();
    OffsetDateTime getUpdatedAt();
    AccountRegisterDTO getRegisterForm();
}
