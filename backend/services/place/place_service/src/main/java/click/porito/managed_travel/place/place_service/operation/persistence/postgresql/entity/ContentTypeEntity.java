package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "content_type")
@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"name"})
public class ContentTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_type_id")
    private Integer contentTypeId;

    @Column(name = "content_type_name", length = 200)
    private String name;

}
