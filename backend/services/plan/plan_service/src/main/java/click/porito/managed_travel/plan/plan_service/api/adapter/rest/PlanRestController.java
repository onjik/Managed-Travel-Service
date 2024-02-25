package click.porito.managed_travel.plan.plan_service.api.adapter.rest;

import click.porito.managed_travel.plan.Plan;
import click.porito.managed_travel.plan.domain.api.PlanApi;
import click.porito.managed_travel.plan.domain.api.reqeust.PlanCreateRequest;
import click.porito.managed_travel.plan.domain.api.reqeust.PlanUpdateRequest;
import click.porito.managed_travel.plan.domain.exception.PlanNotFoundException;
import click.porito.managed_travel.plan.domain.exception.PlanNotModifiedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plans")
public class PlanRestController {

    private final PlanApi planApi;
    @PostMapping
    public ResponseEntity<Plan> createPlan(
            @RequestBody PlanCreateRequest body,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        Plan plan = planApi.createPlan(userId, body);

        String version = plan.getVersion();
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(version)
                .body(plan);
    }
    @GetMapping
    public ResponseEntity<List<Plan>> getPlanIdOwnedBy(
            @RequestParam(name = "ownerId") String ownerId,
            Pageable pageable
    ) {
        List<Plan> plansOwnedBy = planApi.getPlansOwnedBy(ownerId, pageable);
        return ResponseEntity.ok(plansOwnedBy);
    }

    @GetMapping("/{planId}")
    public Plan getPlan(@PathVariable String planId,
                        @RequestHeader(value = "If-None-Match",required = false) String version
    ) throws PlanNotModifiedException, PlanNotFoundException {
        Plan plan = planApi.getPlan(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        if (version != null && version.equals(plan.getVersion())) {
            throw new PlanNotModifiedException(planId);
        }
        return plan;
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable String planId
    ) throws PlanNotFoundException {

        planApi.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{planId}")
    public ResponseEntity<Plan> updatePlan(@PathVariable String planId,
                                           @RequestBody PlanUpdateRequest body,
                                           @RequestHeader(value = "If-Match",required = false) String version
    ) throws PlanNotFoundException {
        final Plan plan = planApi.updatePlan(planId, body);

        String newVersion = plan.getVersion();
        return ResponseEntity.ok()
                .eTag(newVersion)
                .body(plan);
    }

}
