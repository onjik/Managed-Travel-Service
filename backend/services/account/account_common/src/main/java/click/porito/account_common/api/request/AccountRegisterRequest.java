package click.porito.account_common.api.request;

import click.porito.account_common.domain.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AccountRegisterRequest(
        @NotBlank
        String name,
        @Email
        String email,
        @NotNull
        Gender gender,
        @NotNull
        @Past
        LocalDate birthDate
) {


}
