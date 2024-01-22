package click.porito.travel_core.plan.api.application.implementation;

import click.porito.travel_core.plan.InvalidUpdateInfoException;
import click.porito.travel_core.plan.PlanNotFoundException;
import click.porito.travel_core.plan.PlanServerException;
import click.porito.travel_core.plan.PlanVersionOutOfDateException;
import click.porito.travel_core.plan.api.application.PlanApi;
import click.porito.travel_core.plan.api.reqeust.PlanCreateRequest;
import click.porito.travel_core.plan.api.reqeust.PlanUpdateRequest;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.operation.application.PlanOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static click.porito.travel_core.global.exception.ErrorCode.PLAN_DB_OPERATION_FAILED;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class PlanApiImpl implements PlanApi {
    private final PlanOperation planOperation;

    @Override
    @PostAuthorize("@planAccessPolicy.canRead(authentication, returnObject.get())")
    public Optional<Plan> getPlan(String planId) {
        Assert.notNull(planId, "planId must not be null");
        try {
            return planOperation.findById(planId);
        } catch (Exception e) {
            throw new PlanServerException(e, PLAN_DB_OPERATION_FAILED);
        }
    }

    @Override
    @PreAuthorize("@planAccessPolicy.canCreate(authentication)")
    public Plan createPlan(String userId, @Valid PlanCreateRequest planCreateRequest) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(planCreateRequest, "planCreateRequest must not be null");
        Plan plan = Plan.builder()
                .title(planCreateRequest.title())
                .startDate(planCreateRequest.startDate())
                .ownerId(userId)
                .build();
        return planOperation.create(plan);
    }

    @Override
    @PreAuthorize("@planAccessPolicy.canReadOwnedBy(authentication, #userId)")
    public Page<Plan> getPlansOwnedBy(String userId, Pageable pageable) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        return planOperation.findAllByOwnerId(userId, pageable);
    }


    @Override
    @PreAuthorize("@planAccessPolicy.canDelete(authentication, #planId)")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletePlan(String planId) {
        Assert.notNull(planId, "planId must not be null");
        planOperation.deleteById(planId);
    }

    @Override
    @Valid
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public Plan updatePlan(String planId,@Valid PlanUpdateRequest planUpdateRequest) throws InvalidUpdateInfoException, PlanVersionOutOfDateException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(planUpdateRequest, "planUpdateRequest must not be null");
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
