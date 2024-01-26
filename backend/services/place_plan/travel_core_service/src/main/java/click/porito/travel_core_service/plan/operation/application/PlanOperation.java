package click.porito.travel_core_service.plan.operation.application;

import click.porito.plan_common.domain.Plan;
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
