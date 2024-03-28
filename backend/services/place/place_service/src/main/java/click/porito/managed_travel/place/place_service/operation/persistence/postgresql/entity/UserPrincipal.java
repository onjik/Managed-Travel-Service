package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;


@Table(name = "user_principal")
@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "userId")
public class UserPrincipal {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "deleted_at")
    private Instant deletedAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Type(StringArrayType.class)
    @Column(name = "roles",
            columnDefinition = "text[]"
    )
    private List<String> roles;



}
