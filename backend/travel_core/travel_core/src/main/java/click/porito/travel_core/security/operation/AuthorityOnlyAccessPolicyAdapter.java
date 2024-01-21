package click.porito.travel_core.security.operation;

import click.porito.travel_core.security.domain.Action;
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



    @Override
    protected boolean hasPermissionToOwnedBy(Action action, String ownerId, AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean hasPermissionToCreate(AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean hasPermissionWithIds(Action action, List<String> targetId, AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean hasPermissionWithTarget(Action action, List<T> target, AccessContext accessContext) {
        return false;
    }
}
