package click.porito.modular_travel.account.internal.dto;

import click.porito.modular_travel.account.internal.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
public class AccountRegisterForm {

    @NotBlank
    private String name;
    @Email
    private String email;
    @NotNull
    private Account.Gender gender;
    @NotNull
    @Past
    private LocalDate birthDate;

    @Builder
    public AccountRegisterForm(String name, String email, Account.Gender gender, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public AccountRegisterForm(AccountRegisterForm accountRegisterForm) {
        this.name = accountRegisterForm.getName();
        this.email = accountRegisterForm.getEmail();
        this.gender = accountRegisterForm.getGender();
        this.birthDate = accountRegisterForm.getBirthDate();
    }

    public void overrideInfo(AccountRegisterForm supplement){
        Optional.ofNullable(supplement.getEmail()).ifPresent(this::setEmail);
        Optional.ofNullable(supplement.getName()).ifPresent(this::setName);
        Optional.ofNullable(supplement.getGender()).ifPresent(this::setGender);
        Optional.ofNullable(supplement.getBirthDate()).ifPresent(this::setBirthDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountRegisterForm that = (AccountRegisterForm) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && getGender() == that.getGender() && Objects.equals(getBirthDate(), that.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getGender(), getBirthDate());
    }
}
