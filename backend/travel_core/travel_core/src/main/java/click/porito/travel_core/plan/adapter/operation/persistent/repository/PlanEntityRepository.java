package click.porito.travel_core.plan.adapter.operation.persistent.repository;

import click.porito.travel_core.plan.adapter.operation.persistent.entity.PlanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanEntityRepository extends MongoRepository<PlanEntity, String> {
    Page<PlanEntity> findAllByOwnerId(String ownerId, Pageable pageable);
}
