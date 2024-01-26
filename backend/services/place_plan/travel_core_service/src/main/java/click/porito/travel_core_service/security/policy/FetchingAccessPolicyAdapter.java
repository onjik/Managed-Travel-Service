package click.porito.travel_core_service.security.policy;

import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ResourceNotFoundException;
import click.porito.place_common.domain.Place;
import click.porito.plan_common.domain.Plan;
import click.porito.travel_core_service.security.domain.AccessContext;
import click.porito.travel_core_service.security.domain.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

import java.util.List;

@Slf4j
public abstract class FetchingAccessPolicyAdapter<T> extends AccessPolicyAdapter<T>{
    protected FetchingAccessPolicyAdapter(@Nullable RoleHierarchy roleHierarchy) {
        super(roleHierarchy);
    }

    protected abstract List<T> fetchTarget(List<String> targetId);

    @Override
    protected boolean hasPermissionWithIds(Action action, List<String> targetId, AccessContext accessContext) {
        List<T> target = fetchTarget(targetId);
        if (target.isEmpty() || target.size() != targetId.size()) {
            if (getTargetClass().isAssignableFrom(Plan.class))
                throw new ResourceNotFoundException(Domain.PLAN);
            else if (getTargetClass().isAssignableFrom(Place.class)) {
                throw new ResourceNotFoundException(Domain.PLACE);
            } else {
                log.warn("Unknown target class : {}", getTargetClass());
                throw new ResourceNotFoundException(Domain.SECURITY);
            }
        }
        return hasPermissionWithTarget(action, target, accessContext);
    }

}
