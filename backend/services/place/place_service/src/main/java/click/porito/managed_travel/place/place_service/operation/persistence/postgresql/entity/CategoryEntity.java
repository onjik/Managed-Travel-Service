package click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity;

import click.porito.managed_travel.place.domain.PlaceCategory;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "category")
@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "categoryName")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private PlaceCategory category;

}
