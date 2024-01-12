package click.porito.travel_core.plan.implementation;

import click.porito.travel_core.optimization.RouteOptimizeService;
import click.porito.travel_core.place.PlaceService;
import click.porito.travel_core.place.cache.implement.PlaceRepository;
import click.porito.travel_core.plan.InvalidUpdateInfoException;
import click.porito.travel_core.plan.PlanOutOfDateException;
import click.porito.travel_core.plan.PlanService;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.dto.PlanCreateForm;
import click.porito.travel_core.plan.dto.PlanPutForm;
import click.porito.travel_core.plan.dto.PlanView;
import click.porito.travel_core.Mapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated //TODO 이거 상속에도 동작하는지 테스트 코드 작성 필요
@RequiredArgsConstructor
public abstract class AbstractPlanService implements PlanService, ApplicationEventPublisherAware {
    private final Mapper<Plan, PlanView> planViewMapper;
    private final RouteOptimizeService optimizeService;
    private final PlaceService placeService;

    private final PlaceRepository placeRepository;
    private ApplicationEventPublisher eventPublisher;


    protected abstract Optional<Plan> getPlanById(String planId);

    @Override
    public Optional<PlanView> getPlan(String planId) {
        Assert.notNull(planId, "planId must not be null");
        return getPlanById(planId)
                .map(planViewMapper::map);
    }


    @Override
    public PlanView createPlan(@Valid PlanCreateForm form) {
        Assert.notNull(form, "planCreateForm must not be null");
        Integer days = form.dayCount();
        String[] placeIds = form.placeIds();

        Plan plan = new Plan();
        plan.setTitle(form.title());
        plan.setStartDate(form.startDate());
        plan.setOwnerId(form.ownerId());

        return null;
    }

    @Override
    public List<PlanView> getPlansOwnedBy(String userId) {
        return null;
    }

    @Override
    public List<String> getPlanIdOwnedBy(String userId, Integer page, Integer size) {
        return null;
    }

    @Override
    public boolean deletePlan(String planId) {
        return false;
    }

    @Override
    public PlanView putPlanInfo(String planId, PlanPutForm planPutForm) throws InvalidUpdateInfoException, PlanOutOfDateException {
        return null;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    protected ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }
}
