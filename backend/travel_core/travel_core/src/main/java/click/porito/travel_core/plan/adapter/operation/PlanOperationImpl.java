package click.porito.travel_core.plan.adapter.operation;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.plan.PlanNotFoundException;
import click.porito.travel_core.plan.application.operation.PlanOperation;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.adapter.operation.persistent.entity.EntityRouteComponent;
import click.porito.travel_core.plan.adapter.operation.persistent.entity.PlanEntity;
import click.porito.travel_core.plan.adapter.operation.persistent.repository.PlanEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlanOperationImpl implements PlanOperation {
    private final Mapper<Plan, PlanEntity> toEntityMapper;
    private final Mapper<PlanEntity, Plan> toDtoMapper;
    private final PlanEntityRepository planEntityRepository;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Plan create(Plan plan) {
        Assert.notNull(plan, "plan must not be null");
        plan.setPlanId(null);
        PlanEntity planEntity = toEntityMapper.map(plan);
        PlanEntity saved = planEntityRepository.save(planEntity);
        return toDtoMapper.map(saved);
    }

    @Override
    public Page<Plan> findAllByOwnerId(String ownerId, Pageable pageable) {
        Assert.notNull(ownerId, "ownerId must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        return planEntityRepository.findAllByOwnerId(ownerId, pageable)
                .map(toDtoMapper::map);
    }

    @Override
    public void deleteById(String planId) {
        Assert.notNull(planId, "planId must not be null");
        planEntityRepository.deleteById(planId);
    }

    @Override
    public Optional<Plan> findById(String planId) {
        Assert.notNull(planId, "planId must not be null");
        return planEntityRepository.findById(planId)
                .map(toDtoMapper::map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Plan update(Plan plan) {
        Assert.notNull(plan, "plan must not be null");
        Assert.notNull(plan.getPlanId(), "planId must not be null");
        //1. 기존 plan을 가져온다.
        String planId = plan.getPlanId();
        PlanEntity planEntity = planEntityRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        //2. 다른 점을 감지하고 업데이트한다.
        PlanEntity updateNeeded = toEntityMapper.map(plan);

        if (planEntity.equals(updateNeeded)) {
            return plan;
        }

        //2-1. title
        if (!planEntity.getTitle().equals(updateNeeded.getTitle())) {
            planEntity.setTitle(updateNeeded.getTitle());
        }
        //2-2. startDate
        if (!planEntity.getStartDate().equals(updateNeeded.getStartDate())) {
            planEntity.setStartDate(updateNeeded.getStartDate());
        }
        //2-3. route
        List<EntityRouteComponent> route = planEntity.getRoute();
        List<EntityRouteComponent> changedRoute = updateNeeded.getRoute();
        if (route.equals(changedRoute)) {
            planEntity.setRoute(changedRoute);
        }

        //3. 업데이트한다.
        PlanEntity updated = planEntityRepository.save(planEntity);
        return toDtoMapper.map(updated);
    }

}
