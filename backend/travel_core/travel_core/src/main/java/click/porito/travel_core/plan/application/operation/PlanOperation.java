package click.porito.travel_core.plan.application.operation;

import click.porito.travel_core.plan.domain.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlanOperation {
    Plan create(Plan plan);
    Page<Plan> findAllByOwnerId(String ownerId, Pageable pageable);

    void deleteById(String planId);

    Optional<Plan> findById(String planId);

    Plan update(Plan plan);
}
