package click.porito.travel_core.plan.implementation;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.optimization.OptimizationProcessingException;
import click.porito.travel_core.optimization.PointEntity;
import click.porito.travel_core.optimization.RouteOptimizeService;
import click.porito.travel_core.place.PlaceRetrieveFailedException;
import click.porito.travel_core.place.PlaceService;
import click.porito.travel_core.place.dto.PlaceView;
import click.porito.travel_core.plan.InvalidUpdateInfoException;
import click.porito.travel_core.plan.PlanFetchException;
import click.porito.travel_core.plan.PlanOutOfDateException;
import click.porito.travel_core.plan.PlanService;
import click.porito.travel_core.plan.dao.PlanRepository;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.domain.RouteComponent;
import click.porito.travel_core.plan.domain.WayPoint;
import click.porito.travel_core.plan.dto.PlanCreateForm;
import click.porito.travel_core.plan.dto.PlanPutForm;
import click.porito.travel_core.plan.dto.PlanView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPlanService implements PlanService {
    private final PlanRepository planRepository;
    private final RouteOptimizeService routeOptimizeService;
    private final PlaceService placeService;
    private final Mapper<Plan,PlanView> planMapper;

    @Override
    public Optional<PlanView> getPlan(String planId) {
        Assert.notNull(planId, "planId must not be null");
        try {
            return planRepository.findById(planId)
                    .map(planMapper::map);
        } catch (Exception e) {
            throw new PlanFetchException("Error Occurred while fetching plan", e);
        }
    }

    @Override
    public PlanView createPlan(PlanCreateForm planCreateForm) {
        Assert.notNull(planCreateForm, "planCreateForm must not be null");
        //not Null
        String title = planCreateForm.title();
        String ownerId = planCreateForm.ownerId();
        //nullable
        LocalDate startDate = planCreateForm.startDate();
        Integer dayCount = planCreateForm.dayCount();
        String[] placeIds = planCreateForm.placeIds();

        final Plan plan = Plan.builder()
                .title(title)
                .ownerId(ownerId)
                .startDate(startDate)
                .build();

        final Supplier<List<RouteComponent>> routeSupplier;

        if (dayCount != null && placeIds != null) {
            routeSupplier = () -> createRoute(placeIds, dayCount);
        } else if (placeIds != null) {
            routeSupplier = () -> createRoute(placeIds);
        } else { // dayCount == null && placeIds == null
            routeSupplier = ArrayList::new;
        }

        try {
            plan.setRoute(routeSupplier.get());
            return planMapper.map(planRepository.save(plan));
        } catch (PlaceRetrieveFailedException e) {
            log.error("Error Occurred while fetching place", e);
            throw new PlanFetchException("Error Occurred while fetching place", e);
        } catch (OptimizationProcessingException e) {
            log.error("Error Occurred while optimizing route", e);
            throw new PlanFetchException("Error Occurred while optimizing route", e);
        } catch (DataAccessException e) {
            log.error("Error Occurred while create plan (Data Access)", e);
            throw new PlanFetchException("Error Occurred while create plan (Data Access)", e);
        } catch (Exception e) {
            log.error("Error Occurred while create plan (Unexpected)", e);
            throw new PlanFetchException("Error Occurred while create plan (Unexpected)", e);
        }
    }

    private List<RouteComponent> createRoute(@NonNull String[] placeIds, @NonNull Integer dayCount) throws PlaceRetrieveFailedException, OptimizationProcessingException {
        Assert.notNull(placeIds, "placeIds must not be null");
        Assert.noNullElements(placeIds, "placeIds must not contain null");
        Assert.notNull(dayCount, "dayCount must not be null");
        Assert.isTrue(dayCount > 0, "dayCount must be positive");

        //fetch places
        List<PlaceView> places = placeService.getPlaces(placeIds);
        //convert to point entity
        List<PointEntity> pointEntities = places.stream()
                .map(this::convertToPointEntity)
                .collect(Collectors.toCollection(ArrayList::new));
        //optimize
        List<List<PointEntity>> lists = routeOptimizeService.reorderByDistanceAndDayCount(pointEntities, dayCount);
        //convert to route component
        List<RouteComponent> routeComponents = new ArrayList<>();
        for (List<PointEntity> list : lists) {
            var dayRoute = list.stream()
                    .map(pointEntity -> new WayPoint(UUID.randomUUID(), pointEntity.id()))
                    .collect(Collectors.toCollection(ArrayList::new));
            Day day = new Day(UUID.randomUUID());
            day.setWayPoints(dayRoute);
            routeComponents.add(day);
        }
        return routeComponents;
    }

    private List<RouteComponent> createRoute(@NonNull String[] placeIds) throws PlanFetchException, OptimizationProcessingException {
        Assert.notNull(placeIds, "placeIds must not be null");
        Assert.noNullElements(placeIds, "placeIds must not contain null");

        //fetch places
        List<PlaceView> places = placeService.getPlaces(placeIds);
        //convert to point entity
        List<PointEntity> pointEntities = places.stream()
                .map(this::convertToPointEntity)
                .collect(Collectors.toCollection(ArrayList::new));
        //optimize
        List<PointEntity> optimized = routeOptimizeService.reorderByDistance(pointEntities);
        //convert to route component
        List<RouteComponent> routeComponents = new ArrayList<>();
        for (PointEntity pointEntity : optimized) {
            WayPoint wayPoint = new WayPoint(UUID.randomUUID(), pointEntity.id());
            routeComponents.add(wayPoint);
        }
        return routeComponents;
    }

    private PointEntity convertToPointEntity(@NonNull PlaceView placeView) {
        return PointEntity.builder()
                .id(placeView.id())
                .latitude(placeView.latitude())
                .longitude(placeView.longitude())
                .build();
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
}
