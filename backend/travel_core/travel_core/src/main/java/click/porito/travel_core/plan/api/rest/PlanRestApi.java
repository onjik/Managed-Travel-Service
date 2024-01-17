package click.porito.travel_core.plan.api.rest;

import click.porito.travel_core.plan.PlanService;
import click.porito.travel_core.plan.dto.*;
import click.porito.travel_core.Mapper;
import click.porito.travel_core.security.PlanAccessDeniedException;
import click.porito.travel_core.security.PlanAccessManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plans")
public class PlanRestApi {
    private final static String X_USER_ID = "X-Authorization-Id";
    private final static String X_USER_ROLES = "X-Authorization-Roles";

    private final PlanAccessManager planAccessManager;
    private final PlanService planService;
    private final Mapper<RouteComponentRequest, RouteComponentUpdateForm> updateFormMapper;
    @PostMapping
    public ResponseEntity<PlanView> createPlan(
            @RequestBody PlanUpdateRequest body,
            @RequestHeader(value = X_USER_ID, required = true) String userId,
            @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) {
        planAccessManager.can(userId, roles)
                .createPlan();

        // id가 모두 비어 있는지 확인
        if (body.route() != null) {
            Optional<RouteComponentRequest> any = body.route().stream()
                    .filter(Objects::nonNull)
                    .filter(c -> c.id() != null && !c.id().isEmpty())
                    .findAny();
            if (any.isPresent()) {
                throw new IllegalArgumentException("route component id must be empty");
            }
        }

        String title = body.title();
        LocalDate startDate = body.startDate();
        List<RouteComponentUpdateForm> route = updateFormMapper.map(body.route());

        PlanUpdateForm createForm = PlanUpdateForm.builder()
                .title(title)
                .startDate(startDate)
                .userId(userId)
                .route(route)
                .build();

        PlanView plan = planService.createPlan(createForm);

        String version = plan.version();
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(version)
                .body(plan);
    }
    @GetMapping
    public ResponseEntity<Page<PlanView>> getPlanIdOwnedBy(
            @RequestParam(name = "ownerId") String ownerId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Range(min = 5, max = 100) Integer size,
            @RequestHeader(value = X_USER_ID, required = true) String userId,
            @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) {
        planAccessManager.can(userId, roles)
                .accessToPlacesOwnedBy(ownerId)
                .read()
                .check();

        Pageable pageable = PageRequest.of(page, size);
        Page<PlanView> plansOwnedBy = planService.getPlansOwnedBy(ownerId, pageable);
        return ResponseEntity.ok(plansOwnedBy);
    }

    @GetMapping("/{planId}")
    public PlanView getPlan(@NotBlank String planId,
                            @RequestHeader(value = "If-None-Match",required = false) String version,
                            @RequestHeader(value = X_USER_ID, required = true) String userId,
                            @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) throws PlanNotModifiedException, PlanNotFoundException, PlanAccessDeniedException {

        planAccessManager.can(userId, roles)
                .accessToPlan(planId)
                .read()
                .check();

        PlanView planView = planService.getPlan(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        if (version != null && version.equals(planView.version())) {
            throw new PlanNotModifiedException(planId);
        }
        return planView;
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@NotBlank String planId,
                                           @RequestHeader(value = X_USER_ID, required = true) String userId,
                                           @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) throws PlanNotFoundException, PlanAccessDeniedException {
        planAccessManager.can(userId, roles)
                .accessToPlan(planId)
                .delete()
                .check();

        planService.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{planId}")
    public ResponseEntity<PlanView> updatePlan(@NotBlank String planId,
                                               @RequestBody PlanUpdateRequest body,
                                               @RequestHeader(value = X_USER_ID, required = true) String userId,
                                               @RequestHeader(value = X_USER_ROLES, required = true) String[] roles,
                                               @RequestHeader(value = "If-Match",required = false) String version
    ) throws PlanNotFoundException, PlanAccessDeniedException {
        // 권한 체크
        planAccessManager.can(userId, roles)
                .accessToPlan(planId)
                .update()
                .check();

        String title = body.title();
        LocalDate startDate = body.startDate();
        List<RouteComponentUpdateForm> route = updateFormMapper.map(body.route());

        PlanUpdateForm planPutForm = PlanUpdateForm.builder()
                .title(title)
                .startDate(startDate)
                .route(route)
                .version(version)
                .build();

        final PlanView planView = planService.updatePlan(planId, planPutForm);

        String newVersion = planView.version();
        return ResponseEntity.ok()
                .eTag(newVersion)
                .body(planView);
    }

}
