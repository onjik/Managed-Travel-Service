package click.porito.account_service.account.operation.adapter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@Table(name = "user_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEntity {

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
    private List<String> roles;

    @Column(name = "created_at")
    private Instant createdAt;

    //Optional Columns
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(name = "gender")
    private GenderProperty gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "picture_uri")
    private String profileImgUri;

    @JsonIgnore
    @Version
    private Long version;

    public AccountEntity(String name, String email, List<String> roles, GenderProperty gender, LocalDate birthDate, String profileImgUri) {
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImgUri = profileImgUri;
    }


    public static Builder builder(String email, List<String> roles){
        return new Builder(email, roles);
    }
    public static Builder builder(String email, String role){
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
        AccountEntity account = (AccountEntity) o;
        return Objects.equals(getUserId(), account.getUserId()) && Objects.equals(getEmail(), account.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }

    public static class Builder {
        private final String email;
        private final List<String> roles;
        private String name;
        private GenderProperty gender;
        private LocalDate birthDate;
        private String pictureUri;

        public Builder(String email, List<String> roles) {
            this.email = email;
            this.roles = roles;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gender(GenderProperty gender){
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

        public AccountEntity build(){
            return new AccountEntity(name, email, roles, gender, birthDate, pictureUri);
        }

    }

}
