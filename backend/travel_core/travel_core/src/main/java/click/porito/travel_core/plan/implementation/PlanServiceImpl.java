package click.porito.travel_core.plan.implementation;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.PlaceService;
import click.porito.travel_core.place.dto.PlaceView;
import click.porito.travel_core.plan.*;
import click.porito.travel_core.plan.api.rest.PlanNotFoundException;
import click.porito.travel_core.plan.dao.PlanRepository;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.domain.RouteComponent;
import click.porito.travel_core.plan.domain.WayPoint;
import click.porito.travel_core.plan.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    // DAO
    private final PlanRepository planRepository;
    private final PlaceService placeService;
    private final Mapper<Plan,PlanView> planMapper;
    private final Mapper<RouteComponentUpdateForm, RouteComponent> updateFormMapper;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public PlanView createPlan(@Valid PlanUpdateForm planUpdateForm) {
        Assert.notNull(planUpdateForm, "planUpdateForm must not be null");
        //not Null
        String title = planUpdateForm.title();
        String userId = planUpdateForm.userId();
        //nullable
        LocalDate startDate = planUpdateForm.startDate();

        final Plan plan = Plan.builder()
                .title(title)
                .ownerId(userId)
                .startDate(startDate)
                .build();

        // route 처리
        List<RouteComponentUpdateForm> route = planUpdateForm.route();
        if (route != null){
            // check placeId exists
            String[] placeIds = route.stream()
                    .filter(Objects::nonNull)
                    .flatMap(c -> {
                        if (c instanceof DayUpdateForm o) {
                            return o.wayPoints().stream();
                        } else if (c instanceof WayPointUpdateForm o) {
                            return Stream.of(o);
                        } else {
                            return Stream.empty();
                        }
                    })
                    .map(WayPointUpdateForm::placeId)
                    .toArray(String[]::new);

            //TODO : 더 효율적인 id로 조회하는 API로 변경 고려
            List<PlaceView> places = placeService.getPlaces(placeIds);
            if (places.size() != placeIds.length) {
                throw new IllegalArgumentException("invalid placeId");
            }

            List<RouteComponent> map = updateFormMapper.map(route);
            plan.setRoute(map);
        }

        try {
            // save
            Plan saved = planRepository.save(plan);
            log.info("Plan created: {}", saved);
            return planMapper.map(saved);
        } catch (DataAccessException e) {
            log.error("Error Occurred while create plan (Data Access)", e);
            throw new PlanCreateProcessingException("Error Occurred while create plan (Data Access)", e);
        } catch (Exception e) {
            log.error("Error Occurred while create plan (Unexpected)", e);
            throw new PlanCreateProcessingException("Error Occurred while create plan (Unexpected)", e);
        }
    }


    @Override
    public Page<PlanView> getPlansOwnedBy(String userId, Pageable pageable) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        return planRepository.findAllByOwnerId(userId, pageable)
                .map(planMapper::map);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletePlan(String planId) {
        Assert.notNull(planId, "planId must not be null");
        try {
            planRepository.deleteById(planId);
        } catch (Exception e) {
            log.error("Error Occurred while delete plan", e);
            throw new PlanDeleteProcessingException("Error Occurred while delete plan", e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public PlanView updatePlan(String planId, @Valid PlanUpdateForm planUpdateForm) throws InvalidUpdateInfoException, PlanVersionOutOfDateException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(planUpdateForm, "planUpdateForm must not be null");

        // check version
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("plan not found"));

        if (planUpdateForm.version() != null && !planUpdateForm.version().equals(plan.getVersion().toString())) {
            throw new PlanVersionOutOfDateException();
        }

        // update
        if (planUpdateForm.title() != null) {
            plan.setTitle(planUpdateForm.title());
        }
        if (planUpdateForm.startDate() != null) {
            plan.setStartDate(planUpdateForm.startDate());
        }
        if (planUpdateForm.route() != null) {
            /*
            기존에 존재하던 placeId들을 제외하고, placeId가 유효한지 확인
            주어진 wayPointId, DayId가 모두 unique 한지 확인
             */
            Set<String> oldPlaceIds = plan.getRoute().stream()
                    .flatMap(c -> {
                        if (c instanceof Day o) {
                            return o.getWayPoints().stream();
                        } else if (c instanceof WayPoint o) {
                            return Stream.of(o);
                        } else {
                            return Stream.empty();
                        }
                    })
                    .map(WayPoint::getPlaceId)
                    .collect(Collectors.toSet());

            List<String> newPlaceIds = planUpdateForm.route().stream()
                    .filter(Objects::nonNull)
                    .flatMap(c -> {
                        if (c instanceof DayUpdateForm o) {
                            return o.wayPoints().stream();
                        } else if (c instanceof WayPointUpdateForm o) {
                            return Stream.of(o);
                        } else {
                            return Stream.empty();
                        }
                    })
                    .map(WayPointUpdateForm::placeId)
                    .filter(Objects::nonNull)
                    .filter(id -> !oldPlaceIds.contains(id))
                    .collect(Collectors.toCollection(ArrayList::new));


            List<RouteComponentUpdateForm> route = planUpdateForm.route();
            List<RouteComponent> map = updateFormMapper.map(route);
            plan.setRoute(map);
        }

        Plan saved = planRepository.save(plan);
        log.info("Plan updated: {}", saved);
        return planMapper.map(saved);
    }

    private List<RouteComponent> makeNewRoute(List<RouteComponent> oldRoute, List<RouteComponentUpdateForm> updateForms) {
        if (updateForms == null) {
            return oldRoute;
        }
        // place id 유효성 확인
        Set<String> oldPlaceIds = oldRoute.stream()
                .flatMap(c -> {
                    if (c instanceof Day o) {
                        return o.getWayPoints().stream();
                    } else if (c instanceof WayPoint o) {
                        return Stream.of(o);
                    } else {
                        return Stream.empty();
                    }
                })
                .map(WayPoint::getPlaceId)
                .collect(Collectors.toSet());

        List<String> newPlaceIds = updateForms.stream()
                .filter(Objects::nonNull)
                .flatMap(c -> {
                    if (c instanceof DayUpdateForm o) {
                        return o.wayPoints().stream();
                    } else if (c instanceof WayPointUpdateForm o) {
                        return Stream.of(o);
                    } else {
                        return Stream.empty();
                    }
                })
                .map(WayPointUpdateForm::placeId)
                .filter(Objects::nonNull)
                .filter(id -> !oldPlaceIds.contains(id))
                .collect(Collectors.toCollection(ArrayList::new));

        List<PlaceView> places = placeService.getPlaces(newPlaceIds.toArray(String[]::new));
        if (places.size() != newPlaceIds.size()) {
            throw new IllegalArgumentException("invalid placeId");
        }

        // day id 중복 확인
        List<String> dayIdBeforeDistinct = updateForms.stream()
                .filter(Objects::nonNull)
                .filter(c -> c instanceof DayUpdateForm)
                .map(RouteComponentUpdateForm::id)
                .toList();
        List<String> dayIdAfterDistinct = dayIdBeforeDistinct.stream()
                .distinct()
                .toList();
        if (dayIdBeforeDistinct.size() != dayIdAfterDistinct.size()) {
            throw new IllegalArgumentException("day id must be unique");
        }

        // wayPoint id 중복 확인
        List<String> wayPointIdBeforeDistinct = updateForms.stream()
                .filter(Objects::nonNull)
                .filter(c -> c instanceof WayPointUpdateForm)
                .map(RouteComponentUpdateForm::id)
                .toList();
        List<String> wayPointIdAfterDistinct = wayPointIdBeforeDistinct.stream()
                .distinct()
                .toList();
        if (wayPointIdBeforeDistinct.size() != wayPointIdAfterDistinct.size()) {
            throw new IllegalArgumentException("wayPoint id must be unique");
        }

        return updateFormMapper.map(updateForms);
    }



}
