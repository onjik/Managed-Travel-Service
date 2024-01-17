package click.porito.travel_core.plan.dao;

import click.porito.travel_core.plan.domain.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends MongoRepository<Plan, String> {
    Page<Plan> findAllByOwnerId(String ownerId, Pageable pageable);
}
