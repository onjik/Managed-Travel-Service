package click.porito.travel_core.plan.application.api.impl;

import click.porito.travel_core.plan.InvalidUpdateInfoException;
import click.porito.travel_core.plan.PlanNotFoundException;
import click.porito.travel_core.plan.PlanServerException;
import click.porito.travel_core.plan.PlanVersionOutOfDateException;
import click.porito.travel_core.plan.application.api.PlanApi;
import click.porito.travel_core.plan.application.api.reqeust.PlanCreateRequest;
import click.porito.travel_core.plan.application.api.reqeust.PlanUpdateRequest;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.application.operation.PlanOperation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static click.porito.travel_core.global.exception.ErrorCode.PLAN_DB_OPERATION_FAILED;

@Service
@Validated
@Valid
@Slf4j
@RequiredArgsConstructor
public class PlanApiImpl implements PlanApi {
    // DAO
    private final PlanOperation planOperation;

    @Override
    public Optional<Plan> getPlan(@NotNull String planId) {
        try {
            return planOperation.findById(planId);
        } catch (Exception e) {
            throw new PlanServerException(e, PLAN_DB_OPERATION_FAILED);
        }
    }

    @Override
    public Plan createPlan(@NotNull String userId, @NotNull PlanCreateRequest planCreateRequest) {
        Plan plan = Plan.builder()
                .title(planCreateRequest.title())
                .startDate(planCreateRequest.startDate())
                .ownerId(userId)
                .build();
        return planOperation.create(plan);
    }

    @Override
    public Page<Plan> getPlansOwnedBy(@NotNull String userId, @NotNull Pageable pageable) {
        return planOperation.findAllByOwnerId(userId, pageable);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletePlan(@NotNull String planId) {
        planOperation.deleteById(planId);
    }

    @Valid
    @Override
    public Plan updatePlan(@NotNull String planId, @NotNull PlanUpdateRequest planUpdateRequest) throws InvalidUpdateInfoException, PlanVersionOutOfDateException {
        Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("plan not found"));
        if (planUpdateRequest.version() != null && !planUpdateRequest.version().equals(plan.getVersion())) {
            throw new PlanVersionOutOfDateException(planId);
        }
        if (planUpdateRequest.title() != null) {
            plan.setTitle(planUpdateRequest.title());
        }
        if (planUpdateRequest.startDate() != null) {
            plan.setStartDate(planUpdateRequest.startDate());
        }
        return planOperation.update(plan);
    }


}
