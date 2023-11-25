package click.porito.modular_travel.account;

import java.time.OffsetDateTime;

public interface TemporaryUserDTO {
    String getEmail();
    OffsetDateTime getUpdatedAt();
    AccountRegisterDTO getRegisterForm();
}
