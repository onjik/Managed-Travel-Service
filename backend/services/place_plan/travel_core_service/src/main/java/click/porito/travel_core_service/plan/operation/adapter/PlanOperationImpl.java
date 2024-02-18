package click.porito.travel_core_service.plan.operation.adapter;

import click.porito.common.util.Mapper;
import click.porito.managed_travel.plan.Plan;
import click.porito.managed_travel.plan.domain.exception.PlanNotFoundException;
import click.porito.travel_core_service.plan.operation.adapter.persistent.entity.EntityRouteComponent;
import click.porito.travel_core_service.plan.operation.adapter.persistent.entity.PlanEntity;
import click.porito.travel_core_service.plan.operation.adapter.persistent.repository.PlanEntityRepository;
import click.porito.travel_core_service.plan.operation.application.PlanOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
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
    //@Transactional(propagation = Propagation.REQUIRED)
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
    public List<Plan> findAllByIds(List<String> planIds) {
        return planEntityRepository.findAllById(planIds)
                .stream()
                .map(toDtoMapper::map)
                .toList();
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED)
    public Plan update(Plan plan) {
        Assert.notNull(plan, "plan must not be null");
        Assert.notNull(plan.getPlanId(), "planId must not be null");
        //1. 기존 plan을 가져온다.
        String planId = plan.getPlanId();
        PlanEntity planEntity = planEntityRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        //2. 다른 점을 감지하고 업데이트한다.
        PlanEntity request = toEntityMapper.map(plan);

        if (planEntity.equals(request)) {
            return plan;
        }

        //2-1. title
        if (!planEntity.getTitle().equals(request.getTitle())) {
            planEntity.setTitle(request.getTitle());
        }
        //2-2. startDate
        if (!planEntity.getStartDate().equals(request.getStartDate())) {
            planEntity.setStartDate(request.getStartDate());
        }
        //2-3. route
        List<EntityRouteComponent> route = planEntity.getRoute();
        List<EntityRouteComponent> requestedRoute = request.getRoute();
        if (!route.equals(requestedRoute)) { // 같지않으면 업데이트한다.
            planEntity.setRoute(requestedRoute);
        }

        //3. 업데이트한다.
        PlanEntity updated = planEntityRepository.save(planEntity);
        return toDtoMapper.map(updated);
    }

}
