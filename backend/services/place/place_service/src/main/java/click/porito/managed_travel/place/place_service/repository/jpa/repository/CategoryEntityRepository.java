package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Integer> {
    CategoryEntity findByCategory(PlaceCategory category);
    List<CategoryEntity> findByCategoryIn(List<PlaceCategory> categories);

}