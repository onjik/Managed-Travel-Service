package click.porito.travel_core.plan.config;

import click.porito.travel_core.access_controll.operation.FetchingAccessPolicyAdapter;
import click.porito.travel_core.access_controll.domain.Action;
import click.porito.travel_core.access_controll.domain.AuthorityMapper;
import click.porito.travel_core.access_controll.domain.Scope;
import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.plan.PlanNotFoundException;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.operation.application.PlanOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlanAccessPolicy extends FetchingAccessPolicyAdapter<Plan> {
    private final PlanOperation planOperation;

    @Override
    public boolean canCreate(Authentication authentication) {
        return getGrantedAuthorities(authentication)
                .stream()
                .anyMatch(a -> a.startsWith("plan:write"));
    }

    @Override
    public boolean canReadOwnedBy(Authentication authentication, String ownerId) {
        return ownerId.equals(authentication.getName());
    }

    @Override
    protected List<Plan> fetchByIds(List<String> targetIds) {
        return planOperation.findAllByIds(targetIds);
    }

    @Override
    protected Plan fetchById(String targetId) {
        return planOperation.findById(targetId)
                .orElseThrow(() -> new PlanNotFoundException(targetId));
    }

    @Override
    protected boolean canAccess(Action action, Plan domainObject, Authentication authentication, Set<AuthorityMapper> authorityMappers) {
        //filtering
        Set<Scope> grantedScopes = authorityMappers.stream()
                .filter(a -> a.domain().equals(Domain.PLAN))
                .filter(a -> a.action().equals(action))
                .map(AuthorityMapper::scope)
                .collect(Collectors.toSet());
        if (grantedScopes.isEmpty()) {
            return false;
        }

        //check
        if (grantedScopes.contains(Scope.ALL)) {
            return true;
        }
        if (grantedScopes.contains(Scope.OWNED) && isOwnedBy(authentication.getName(), domainObject)) {
            return true;
        }
        return grantedScopes.contains(Scope.BELONGED) && isBelongedTo(authentication.getName(), domainObject);
    }

    private boolean isOwnedBy(String userId, Plan plan){
        return userId.equals(plan.getOwnerId());
    }
    private boolean isBelongedTo(String userId, Plan plan){
        return userId.equals(plan.getOwnerId()); //TODO 조인 기능 구현시 수정
    }


    @Override
    protected Class<Plan> getSupportedClass() {
        return Plan.class;
    }
}

