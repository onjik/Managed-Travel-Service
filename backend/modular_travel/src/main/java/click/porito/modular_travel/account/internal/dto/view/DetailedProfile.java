package click.porito.modular_travel.account.internal.dto.view;

import click.porito.modular_travel.account.internal.entity.Account;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class DetailedProfile {
    private Long userId;
    private String name;
    private String email;
    private Instant createdAt;
    private Account.Gender gender;
    private LocalDate birthDate;
    private String profileImgUri;

    public DetailedProfile(Long userId, String name, String email, Instant createdAt, Account.Gender gender, LocalDate birthDate, String profileImgUri) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImgUri = profileImgUri;
    }

    public static DetailedProfile from(Account account) {
        return new DetailedProfile(
                account.getUserId(),
                account.getName(),
                account.getEmail(),
                account.getCreatedAt(),
                account.getGender(),
                account.getBirthDate(),
                account.getProfileImgUri()
        );
    }
}
