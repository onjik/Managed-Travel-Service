package click.porito.travel_plan_service.dao;

import click.porito.travel_plan_service.domain.Plan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends MongoRepository<Plan, String> {
}
