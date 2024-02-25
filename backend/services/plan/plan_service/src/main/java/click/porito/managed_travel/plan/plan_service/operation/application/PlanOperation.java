package click.porito.managed_travel.plan.plan_service.operation.application;

import click.porito.managed_travel.plan.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PlanOperation {
    Plan create(Plan plan);
    Page<Plan> findAllByOwnerId(String ownerId, Pageable pageable);

    void deleteById(String planId);

    Optional<Plan> findById(String planId);
    List<Plan> findAllByIds(List<String> planIds);

    Plan update(Plan plan);
}
