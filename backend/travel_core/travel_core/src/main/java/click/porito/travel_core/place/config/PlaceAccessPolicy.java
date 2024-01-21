package click.porito.travel_core.place.config;

import click.porito.travel_core.security.domain.Action;
import click.porito.travel_core.security.domain.Scope;
import click.porito.travel_core.security.operation.AccessContext;
import click.porito.travel_core.security.operation.AuthorityOnlyAccessPolicyAdapter;
import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.place.domain.Place;
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
