package click.porito.managed_travel.plan.plan_service.operation.adapter;

import click.porito.managed_travel.plan.Plan;
import click.porito.managed_travel.plan.plan_service.operation.application.PlanOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MockPlanOperation implements PlanOperation {
    final List<Plan> plans;
    @Override
    public Plan create(Plan plan) {
        if (plan.getPlanId() == null){
            plan.setPlanId("planId");
        }
        return plan;
    }

    @Override
    public Page<Plan> findAllByOwnerId(String ownerId, Pageable pageable) {
        List<Plan> plans = this.plans.stream()
                .filter(plan -> plan.getOwnerId().equals(ownerId))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
        return new PageImpl<>(plans, pageable, this.plans.size());
    }

    @Override
    public void deleteById(String planId) {
        plans.removeIf(plan -> plan.getPlanId().equals(planId));
    }

    @Override
    public Optional<Plan> findById(String planId) {
        return plans.stream()
                .filter(plan -> plan.getPlanId().equals(planId))
                .findFirst();
    }

    @Override
    public List<Plan> findAllByIds(List<String> planIds) {
        return plans.stream()
                .filter(plan -> planIds.contains(plan.getPlanId()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Plan update(Plan plan) {
        if (plan.getPlanId() == null){
            plan.setPlanId("planId");
        }
        return plan;
    }
}
