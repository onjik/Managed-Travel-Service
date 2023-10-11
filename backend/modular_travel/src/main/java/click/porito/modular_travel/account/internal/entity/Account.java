package click.porito.modular_travel.account.internal.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.Assert;
import click.porito.modular_travel.account.internal.util.KoreanNameGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@Table(name = "user_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key",updatable = false)
    private Long userId;

    //NotNull Columns
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Type(
            value = ListArrayType.class,
            parameters = @Parameter(
                    name = AbstractArrayType.SQL_ARRAY_TYPE,
                    value = "granted_role"
            )
    )
    @Column(name = "roles",
            columnDefinition = "granted_role[]"
    )
    private List<Role> roles;

    @Column(name = "created_at")
    private Instant createdAt;

    //Optional Columns
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    @Column(name = "picture_uri", nullable = true)
    private String profileImgUri;

    @Column(name = "email_verified",nullable = true)
    private Boolean emailVerified;

    protected Account(String name, String email, List<Role> roles, Gender gender, LocalDate birthDate, String profileImgUri, Boolean emailVerified) {
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImgUri = profileImgUri;
        this.emailVerified = emailVerified;
    }

    public static Account from(OidcUser oidcUser, Role role){
        return from(oidcUser, List.of(role));
    }
    public static Account from(OidcUser oidcUser, List<Role> roles){
        Assert.notNull(oidcUser, "oidcUser must not be null");
        Assert.notNull(oidcUser.getEmail(), "oidcUser.getEmail() must not be null");
        Assert.notNull(roles, "roles must not be null");
        Assert.isTrue(roles.size() > 0, "roles must not be empty");
        return new Builder(oidcUser.getEmail(), roles)
                .name(KoreanNameGenerator.generate())
                .gender(oidcUser.getGender() != null ? Gender.valueOf(oidcUser.getGender().toUpperCase()) : null)
                .pictureUri(oidcUser.getPicture())
                .emailVerified(oidcUser.getEmailVerified())
                .build();
    }

    public static Builder builder(String email, List<Role> roles){
        return new Builder(email, roles);
    }
    public static Builder builder(String email, Role role){
        return new Builder(email, List.of(role));
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return getEmail().equals(account.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    public static class Builder {
        private final String email;
        private final List<Role> roles;
        private String name;
        private Gender gender;
        private LocalDate birthDate;
        private String pictureUri;
        private Boolean emailVerified;

        public Builder(String email, List<Role> roles) {
            this.email = email;
            this.roles = roles;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gender(Gender gender){
            this.gender = gender;
            return this;
        }

        public Builder birthDate(LocalDate birthDate){
            this.birthDate = birthDate;
            return this;
        }

        public Builder pictureUri(String pictureUri){
            this.pictureUri = pictureUri;
            return this;
        }

        public Builder emailVerified(Boolean emailVerified){
            this.emailVerified = emailVerified;
            return this;
        }

        public Account build(){
            return new Account(name, email, roles, gender, birthDate, pictureUri, emailVerified);
        }


    }

    public enum Gender {
        MALE, FEMALE;

        @Override
        public String toString() {
            return this.name();
        }
    }

    public enum Role implements GrantedAuthority {
        ROLE_ADMIN, ROLE_USER;

        @Override
        public String getAuthority() {
            return this.name();
        }
    }
}
