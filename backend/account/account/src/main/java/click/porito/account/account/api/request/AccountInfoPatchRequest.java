package click.porito.account.account.api.request;

import click.porito.account.account.domain.Gender;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record AccountInfoPatchRequest(
        @Length(min = 2, max = 20)
        String name,

        Gender gender,

        @Past
        LocalDate birthDate
) {
}
