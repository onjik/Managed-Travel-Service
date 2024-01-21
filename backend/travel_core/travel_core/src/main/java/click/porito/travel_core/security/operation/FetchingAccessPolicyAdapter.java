package click.porito.travel_core.security.operation;

import click.porito.travel_core.security.ResourceNotFoundException;
import click.porito.travel_core.security.domain.Action;
import org.springframework.lang.Nullable;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

import java.util.List;

public abstract class FetchingAccessPolicyAdapter<T> extends AccessPolicyAdapter<T>{
    protected FetchingAccessPolicyAdapter(@Nullable RoleHierarchy roleHierarchy) {
        super(roleHierarchy);
    }

    protected abstract List<T> fetchTarget(List<String> targetId);

    @Override
    protected boolean hasPermissionWithIds(Action action, List<String> targetId, AccessContext accessContext) {
        List<T> target = fetchTarget(targetId);
        if (target.isEmpty() || target.size() != targetId.size()) {
            throw new ResourceNotFoundException();
        }
        return hasPermissionWithTarget(action, target, accessContext);
    }

}
