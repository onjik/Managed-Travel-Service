package click.porito.travel_core.plan.api.adapter.rest;

import click.porito.travel_core.plan.PlanNotFoundException;
import click.porito.travel_core.plan.PlanNotModifiedException;
import click.porito.travel_core.plan.api.application.PlanApi;
import click.porito.travel_core.plan.api.reqeust.PlanCreateRequest;
import click.porito.travel_core.plan.api.reqeust.PlanUpdateRequest;
import click.porito.travel_core.plan.domain.Plan;
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

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plans")
public class PlanRestController {
    private final static String X_USER_ID = "X-Authorization-Id";
    private final static String X_USER_ROLES = "X-Authorization-Roles";

    private final PlanApi planApi;
    @PostMapping
    public ResponseEntity<Plan> createPlan(
            @RequestBody PlanCreateRequest body,
            @RequestHeader(value = X_USER_ID, required = true) String userId,
            @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) {
        Plan plan = planApi.createPlan(userId, body);

        String version = plan.getVersion();
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(version)
                .body(plan);
    }
    @GetMapping
    public ResponseEntity<Page<Plan>> getPlanIdOwnedBy(
            @RequestParam(name = "ownerId") String ownerId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Range(min = 5, max = 100) Integer size,
            @RequestHeader(value = X_USER_ID, required = true) String userId,
            @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Plan> plansOwnedBy = planApi.getPlansOwnedBy(ownerId, pageable);
        return ResponseEntity.ok(plansOwnedBy);
    }

    @GetMapping("/{planId}")
    public Plan getPlan(@NotBlank String planId,
                        @RequestHeader(value = "If-None-Match",required = false) String version,
                        @RequestHeader(value = X_USER_ID, required = true) String userId,
                        @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) throws PlanNotModifiedException, PlanNotFoundException {
        Plan plan = planApi.getPlan(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        if (version != null && version.equals(plan.getVersion())) {
            throw new PlanNotModifiedException(planId);
        }
        return plan;
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@NotBlank String planId,
                                           @RequestHeader(value = X_USER_ID, required = true) String userId,
                                           @RequestHeader(value = X_USER_ROLES, required = true) String[] roles
    ) throws PlanNotFoundException {

        planApi.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{planId}")
    public ResponseEntity<Plan> updatePlan(@NotBlank String planId,
                                           @RequestBody PlanUpdateRequest body,
                                           @RequestHeader(value = X_USER_ID, required = true) String userId,
                                           @RequestHeader(value = X_USER_ROLES, required = true) String[] roles,
                                           @RequestHeader(value = "If-Match",required = false) String version
    ) throws PlanNotFoundException {
        final Plan plan = planApi.updatePlan(planId, body);

        String newVersion = plan.getVersion();
        return ResponseEntity.ok()
                .eTag(newVersion)
                .body(plan);
    }

}
