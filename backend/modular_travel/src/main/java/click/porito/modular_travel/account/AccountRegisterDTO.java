package click.porito.modular_travel.account;

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
public class AccountRegisterDTO {

    @NotBlank
    private String name;
    @Email
    private String email;
    @NotNull
    private Gender gender;
    @NotNull
    @Past
    private LocalDate birthDate;

    @Builder
    public AccountRegisterDTO(String name, String email, Gender gender, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public AccountRegisterDTO(AccountRegisterDTO accountRegisterDTO) {
        this.name = accountRegisterDTO.getName();
        this.email = accountRegisterDTO.getEmail();
        this.gender = accountRegisterDTO.getGender();
        this.birthDate = accountRegisterDTO.getBirthDate();
    }

    public void overrideInfo(AccountRegisterDTO supplement){
        Optional.ofNullable(supplement.getEmail()).ifPresent(this::setEmail);
        Optional.ofNullable(supplement.getName()).ifPresent(this::setName);
        Optional.ofNullable(supplement.getGender()).ifPresent(this::setGender);
        Optional.ofNullable(supplement.getBirthDate()).ifPresent(this::setBirthDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountRegisterDTO that = (AccountRegisterDTO) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && getGender() == that.getGender() && Objects.equals(getBirthDate(), that.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getGender(), getBirthDate());
    }
}
