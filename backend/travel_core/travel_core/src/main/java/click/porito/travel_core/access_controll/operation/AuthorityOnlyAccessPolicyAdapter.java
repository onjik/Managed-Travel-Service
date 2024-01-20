package click.porito.travel_core.access_controll.operation;

import click.porito.travel_core.place.PlaceNotFoundException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public abstract class AuthorityOnlyAccessPolicyAdapter<T> implements AccessPolicy<T> {
    protected abstract boolean canCreateInternal(Set<String> grantedAuthorities);
    protected abstract boolean canReadInternal(Set<String> grantedAuthorities);
    protected abstract boolean canUpdateInternal(Set<String> grantedAuthorities);
    protected abstract boolean canDeleteInternal(Set<String> grantedAuthorities);




    @Override
    public boolean canCreate(Authentication authentication) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canCreateInternal(grantedAuthorities);
    }

    @Override
    public boolean canRead(Authentication authentication, String targetId) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canReadInternal(grantedAuthorities);
    }

    @Override
    public boolean canRead(Authentication authentication, List<String> targetIds) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canReadInternal(grantedAuthorities);
    }

    @Override
    public boolean canReadOwnedBy(Authentication authentication, String ownerId) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canReadInternal(grantedAuthorities);
    }

    @Override
    public boolean canUpdate(Authentication authentication, String targetId) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canUpdateInternal(grantedAuthorities);
    }

    @Override
    public boolean canUpdate(Authentication authentication, List<String> targetIds) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canUpdateInternal(grantedAuthorities);
    }

    @Override
    public boolean canUpdate(Authentication authentication, T target) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canUpdateInternal(grantedAuthorities);
    }

    @Override
    public boolean canDelete(Authentication authentication, String targetId) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canDeleteInternal(grantedAuthorities);
    }

    @Override
    public boolean canDelete(Authentication authentication, T target) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canDeleteInternal(grantedAuthorities);
    }

    @Override
    public boolean canDelete(Authentication authentication, List<String> targetIds) throws PlaceNotFoundException {
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return canDeleteInternal(grantedAuthorities);
    }
}
