package click.porito.travel_core.security.policy;

import click.porito.travel_core.security.domain.Action;
import click.porito.travel_core.security.domain.AccessContext;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

import java.util.List;

public abstract class AuthorityOnlyAccessPolicyAdapter<T> extends AccessPolicyAdapter<T> {

    public AuthorityOnlyAccessPolicyAdapter(RoleHierarchy roleHierarchy) {
        super(roleHierarchy);
    }

    protected abstract boolean canCreate(AccessContext accessContext);
    protected abstract boolean canRead(AccessContext accessContext);
    protected abstract boolean canUpdate(AccessContext accessContext);
    protected abstract boolean canDelete(AccessContext accessContext);

    private boolean dispatch(Action action, AccessContext accessContext){
        return switch (action) {
            case CREATE -> canCreate(accessContext);
            case READ -> canRead(accessContext);
            case UPDATE -> canUpdate(accessContext);
            case DELETE -> canDelete(accessContext);
            default -> false;
        };
    }

    @Override
    protected boolean hasPermissionToCreate(AccessContext accessContext) {
        return canCreate(accessContext);
    }

    @Override
    protected boolean hasPermissionWithIds(Action action, List<String> targetId, AccessContext accessContext) {
        return dispatch(action, accessContext);
    }

    @Override
    protected boolean hasPermissionWithTarget(Action action, List<T> target, AccessContext accessContext) {
        return dispatch(action, accessContext);
    }
}
