package click.porito.travel_core_service.place.config;

import click.porito.common.exception.Domain;
import click.porito.place_common.domain.Place;
import click.porito.travel_core_service.security.domain.AccessContext;
import click.porito.travel_core_service.security.domain.Action;
import click.porito.travel_core_service.security.domain.Scope;
import click.porito.travel_core_service.security.policy.AuthorityOnlyAccessPolicyAdapter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.stereotype.Component;

/**
 * 현재로서 Place 는 별다른 정책은 존재하지 않는다. 그냥 읽기 권한이 있으면, 읽기 가능
 */
@Component
public class PlaceAccessPolicy extends AuthorityOnlyAccessPolicyAdapter<Place> {
    public PlaceAccessPolicy(RoleHierarchy roleHierarchy) {
        super(roleHierarchy);
    }

    @Override
    protected boolean hasPermissionToOwnedBy(Action action, String ownerId, AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean canCreate(AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean canRead(AccessContext accessContext) {
        return accessContext.getPermissionAuthoritySet().stream()
                .filter(scopeAuthority -> Domain.PLACE.equals(scopeAuthority.domain()))
                .filter(scopeAuthority -> Action.READ.equals(scopeAuthority.action()))
                .anyMatch(scopeAuthority -> Scope.ALL.equals(scopeAuthority.scope()));
    }

    @Override
    protected boolean canUpdate(AccessContext accessContext) {
        return false;
    }

    @Override
    protected boolean canDelete(AccessContext accessContext) {
        return false;
    }
}
