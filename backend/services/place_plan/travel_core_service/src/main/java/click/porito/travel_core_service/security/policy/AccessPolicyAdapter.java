package click.porito.travel_core_service.security.policy;

import click.porito.common.exception.common.ResourceNotFoundException;
import click.porito.travel_core_service.security.domain.AccessContext;
import click.porito.travel_core_service.security.domain.Action;
import click.porito.travel_core_service.security.operation.AccessContextFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.lang.Nullable;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.util.List;

public abstract class AccessPolicyAdapter<T> implements AccessPolicy<T>{

    protected Class<T> targetClass;
    protected static final boolean PERMIT_ALL = true;
    protected static final boolean DENY_ALL = false;
    private final AccessContextFactory accessContextFactory;

    @SuppressWarnings("unchecked")
    public AccessPolicyAdapter(@Nullable RoleHierarchy roleHierarchy) {
        this.targetClass = (Class<T>) GenericTypeResolver.resolveType(this.getClass(), AccessPolicyAdapter.class);
        var trustResolver = new AuthenticationTrustResolverImpl();
        roleHierarchy = roleHierarchy != null ? roleHierarchy : new NullRoleHierarchy();
        this.accessContextFactory = new AccessContextFactory(trustResolver, roleHierarchy);
    }

    protected Class<T> getTargetClass() {
        return targetClass;
    }


    protected abstract boolean hasPermissionToOwnedBy(Action action, String ownerId, AccessContext accessContext);
    protected abstract boolean hasPermissionToCreate(AccessContext accessContext);
    protected abstract boolean hasPermissionWithIds(Action action, List<String> targetId, AccessContext accessContext);
    protected abstract boolean hasPermissionWithTarget(Action action, List<T> target, AccessContext accessContext);

    protected boolean hasPermissionWithId(Action action, List<String> targetId, Authentication authentication){
        AccessContext accessContext = accessContextFactory.create(authentication);
        return hasPermissionWithIds(action, targetId, accessContext);
    }

    protected boolean hasPermissionWithTarget(Action action, List<T> target, Authentication authentication){
        AccessContext accessContext = accessContextFactory.create(authentication);
        return hasPermissionWithTarget(action, target, accessContext);
    }

    protected boolean hasPermissionToCreate(Authentication authentication){
        AccessContext accessContext = accessContextFactory.create(authentication);
        return hasPermissionToCreate(accessContext);
    }

    protected boolean hasPermissionToOwnedBy(Action action, String ownerId, Authentication authentication){
        AccessContext accessContext = accessContextFactory.create(authentication);
        return hasPermissionToOwnedBy(action, ownerId, accessContext);
    }



    @Override
    public boolean canCreate(Authentication authentication) throws ResourceNotFoundException {
        return hasPermissionToCreate(authentication);
    }

    @Override
    public boolean canRead(Authentication authentication, String targetId) throws ResourceNotFoundException {
        return hasPermissionWithId(Action.READ, List.of(targetId), authentication);
    }

    @Override
    public boolean canRead(Authentication authentication, T target) throws ResourceNotFoundException {
        return hasPermissionWithTarget(Action.READ, List.of(target), authentication);
    }

    @Override
    public boolean canRead(Authentication authentication, List<String> targetIds) throws ResourceNotFoundException {
        return hasPermissionWithId(Action.READ, targetIds, authentication);
    }

    @Override
    public boolean canReadOwnedBy(Authentication authentication, String ownerId) throws ResourceNotFoundException {
        return hasPermissionToOwnedBy(Action.READ, ownerId, authentication);
    }

    @Override
    public boolean canUpdate(Authentication authentication, String targetId) throws ResourceNotFoundException {
        return hasPermissionWithId(Action.UPDATE, List.of(targetId), authentication);
    }

    @Override
    public boolean canUpdate(Authentication authentication, List<String> targetIds) throws ResourceNotFoundException {
        return hasPermissionWithId(Action.UPDATE, targetIds, authentication);
    }

    @Override
    public boolean canUpdate(Authentication authentication, T target) throws ResourceNotFoundException {
        return hasPermissionWithTarget(Action.UPDATE, List.of(target), authentication);
    }

    @Override
    public boolean canDelete(Authentication authentication, String targetId) throws ResourceNotFoundException {
        return hasPermissionWithId(Action.DELETE, List.of(targetId), authentication);
    }

    @Override
    public boolean canDelete(Authentication authentication, T target) throws ResourceNotFoundException {
        return hasPermissionWithTarget(Action.DELETE, List.of(target), authentication);
    }

    @Override
    public boolean canDelete(Authentication authentication, List<String> targetIds) throws ResourceNotFoundException {
        return hasPermissionWithId(Action.DELETE, targetIds, authentication);
    }

}
