package click.porito.managed_travel.plan.plan_service.operation.adapter.persistent.repository;

import click.porito.managed_travel.plan.plan_service.operation.adapter.persistent.entity.PlanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanEntityRepository extends MongoRepository<PlanEntity, String> {
    Page<PlanEntity> findAllByOwnerId(String ownerId, Pageable pageable);
}
