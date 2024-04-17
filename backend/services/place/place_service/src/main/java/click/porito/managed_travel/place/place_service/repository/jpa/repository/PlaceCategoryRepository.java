package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceCategoryRepository extends org.springframework.data.repository.Repository<CategoryEntity, Integer> {
    List<CategoryEntity> findByCategoryIn(List<PlaceCategory> categories);

}
