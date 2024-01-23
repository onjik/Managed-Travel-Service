package click.porito.travel_core.plan.config;

import click.porito.travel_core.security.domain.Action;
import click.porito.travel_core.security.domain.Scope;
import click.porito.travel_core.security.domain.PermissionAuthority;
import click.porito.travel_core.security.domain.AccessContext;
import click.porito.travel_core.security.policy.FetchingAccessPolicyAdapter;
import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.operation.application.PlanOperation;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlanAccessPolicy extends FetchingAccessPolicyAdapter<Plan>{
    private final PlanOperation planOperation;

    protected PlanAccessPolicy(RoleHierarchy roleHierarchy, PlanOperation planOperation) {
        super(roleHierarchy);
        this.planOperation = planOperation;
    }


    @Override
    protected boolean hasPermissionToOwnedBy(Action action, String ownerId, AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean hasPermissionToCreate(AccessContext accessContext) {
        return accessContext.getPermissionAuthoritySet().stream()
                .filter(permissionAuthority -> Domain.PLAN.equals(permissionAuthority.domain()))
                .anyMatch(permissionAuthority -> Action.CREATE.equals(permissionAuthority.action()));
    }

    @Override
    protected boolean hasPermissionWithTarget(Action action, List<Plan> target, AccessContext accessContext) {
        Set<Scope> allowedScope = accessContext.getPermissionAuthoritySet().stream()
                .filter(permissionAuthority -> Domain.PLAN.equals(permissionAuthority.domain()))
                .filter(permissionAuthority -> action.equals(permissionAuthority.action()))
                .map(PermissionAuthority::scope)
                .collect(Collectors.toSet());
        String userId = accessContext.getUserId();

        if (allowedScope.isEmpty()) {
            return false;
        }
        if (allowedScope.contains(Scope.ALL)) {
            return true;
        }

        if (allowedScope.contains(Scope.OWNED) && isOwnerOfAll(userId, target)) {
            return true;
        }

        if (allowedScope.contains(Scope.BELONGED) && isBelongedOfAll(userId, target)) {
            return true;
        }

        return false;
    }

    private boolean isOwner(String userId, Plan plan) {
        return userId.equals(plan.getOwnerId());
    }

    private boolean isOwnerOfAll(String userId, List<Plan> plans) {
        return plans.stream()
                .allMatch(plan -> userId.equals(plan.getOwnerId()));
    }

    private boolean isBelonged(String userId, Plan plan) {
        return isOwner(userId, plan); // TODO 현재는 조인 로직이 구현되어 있지 않아서, 이렇게 처리, 추후 구현시 수정 필요
    }

    private boolean isBelongedOfAll(String userId, List<Plan> plans) {
        return isOwnerOfAll(userId, plans); // TODO 현재는 조인 로직이 구현되어 있지 않아서, 이렇게 처리, 추후 구현시 수정 필요
    }

    @Override
    protected List<Plan> fetchTarget(List<String> targetId) {
        return planOperation.findAllByIds(targetId);
    }

}

