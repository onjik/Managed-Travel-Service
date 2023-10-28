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
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@Table(name = "user_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id",updatable = false)
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
                    value = "text"
            )
    )
    @Column(name = "roles",
            columnDefinition = "text[]"
    )
    private List<Role> roles;

    @Column(name = "created_at")
    private Instant createdAt;

    //Optional Columns
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "picture_uri")
    private String profileImgUri;

    @Version
    private Long version;

    protected Account(String name, String email, List<Role> roles, Gender gender, LocalDate birthDate, String profileImgUri) {
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImgUri = profileImgUri;
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

    public OidcUser toOidcUser(final OidcIdToken idToken){
        Assert.notEmpty(idToken.getClaims(), "claims must not be empty");

        //idToken 의 sub(Subject) 을 account 의 userId 로 변경 하여 다시 생성
        HashMap<String, Object> map = new HashMap<>(idToken.getClaims());
        map.put(IdTokenClaimNames.SUB, this.getUserId());
        OidcIdToken token = new OidcIdToken(
                idToken.getTokenValue(),
                idToken.getIssuedAt(),
                idToken.getExpiresAt(),
                Collections.unmodifiableMap(map)
        );
        return new DefaultOidcUser(this.getRoles(), token);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getUserId(), account.getUserId()) && Objects.equals(getEmail(), account.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }

    public static class Builder {
        private final String email;
        private final List<Role> roles;
        private String name;
        private Gender gender;
        private LocalDate birthDate;
        private String pictureUri;

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

        public Account build(){
            return new Account(name, email, roles, gender, birthDate, pictureUri);
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
        ADMIN, USER;
        private String authority;
        Role() {
            this.authority = "ROLE_" + this.name();
        }

        @Override
        public String getAuthority() {
            return this.authority;
        }
    }
}
