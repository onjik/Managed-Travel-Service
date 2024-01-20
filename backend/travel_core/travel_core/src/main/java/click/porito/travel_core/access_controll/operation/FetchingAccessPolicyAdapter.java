package click.porito.travel_core.access_controll.operation;

import click.porito.travel_core.access_controll.domain.Action;
import click.porito.travel_core.access_controll.domain.AuthorityMapper;
import click.porito.travel_core.place.PlaceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FetchingAccessPolicyAdapter<T> implements AccessPolicy<T>{
    /**
     * @param targetIds 조회할 대상의 id
     * @return 일부가 없거나, 빈 리스트가 반환될 수 있음. never null
     */
    protected abstract List<T> fetchByIds(List<String> targetIds);

    /**
     * @param targetId 조회할 대상의 id
     * @return 반드시 객체 리턴, 없으면 PlaceNotFoundException 발생 시켜야 함
     * @throws PlaceNotFoundException 해당 id에 대한 객체가 없을 때 발생
     */
    protected abstract T fetchById(String targetId) throws PlaceNotFoundException;
    protected abstract boolean canAccess(Action action, T domainObject, Authentication authentication, Set<AuthorityMapper> authorityMappers);

    protected abstract Class<T> getSupportedClass();

    protected Set<AuthorityMapper> mapToAuthorityMappers(Authentication authentication){
        Set<String> grantedAuthorities = getGrantedAuthorities(authentication);
        return grantedAuthorities.stream()
                .map(AuthorityMapper::of)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    protected boolean canReadInternal(Authentication authentication, T domainObject){
        Set<AuthorityMapper> authorityMappers = mapToAuthorityMappers(authentication);
        return canAccess(Action.READ, domainObject, authentication, authorityMappers);
    }
    protected boolean canUpdateInternal(Authentication authentication, T domainObject){
        Set<AuthorityMapper> authorityMappers = mapToAuthorityMappers(authentication);
        return canAccess(Action.UPDATE, domainObject, authentication, authorityMappers);
    }
    protected boolean canDeleteInternal(Authentication authentication, T domainObject){
        Set<AuthorityMapper> authorityMappers = mapToAuthorityMappers(authentication);
        return canAccess(Action.DELETE, domainObject, authentication, authorityMappers);
    }

    @Override
    public boolean canRead(Authentication authentication, String targetId){
        T target = fetchById(targetId);
        return canReadInternal(authentication, target);
    }


    @Override
    public boolean canRead(Authentication authentication, List<String> targetIds){
        List<T> targets = fetchByIds(targetIds);
        return targets.stream().allMatch(t -> canReadInternal(authentication, t));
    }

    @Override
    public boolean canUpdate(Authentication authentication, String targetId){
        T target = fetchById(targetId);
        return canUpdateInternal(authentication, target);
    }

    @Override
    public boolean canUpdate(Authentication authentication, T target){
        return canUpdateInternal(authentication, target);
    }

    @Override
    public boolean canUpdate(Authentication authentication, List<String> targetIds){
        List<T> targets = fetchByIds(targetIds);
        return targets.stream().allMatch(t -> canUpdateInternal(authentication, t));
    }

    @Override
    public boolean canDelete(Authentication authentication, String targetId){
        T target = fetchById(targetId);
        return canDeleteInternal(authentication, target);
    }

    @Override
    public boolean canDelete(Authentication authentication, T target){
        Assert.isInstanceOf(getSupportedClass(),target);
        return canDeleteInternal(authentication, target);
    }

    @Override
    public boolean canDelete(Authentication authentication, List<String> targetIds){
        List<T> targets = fetchByIds(targetIds);
        return targets.stream().allMatch(t -> canDeleteInternal(authentication, t));
    }
}
