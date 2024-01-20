package click.porito.travel_core.place.config;

import click.porito.travel_core.access_controll.operation.AuthorityOnlyAccessPolicyAdapter;
import click.porito.travel_core.place.domain.Place;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 현재로서 Place 는 별다른 정책은 존재하지 않는다. 그냥 읽기 권한이 있으면, 읽기 가능
 */
@Component
public class PlaceAccessPolicy extends AuthorityOnlyAccessPolicyAdapter<Place> {
    private final static String PLACE_READ_ALL = "place:read:all";


    @Override
    protected boolean canCreateInternal(Set<String> grantedAuthorities) {
        return false;
    }

    @Override
    protected boolean canReadInternal(Set<String> grantedAuthorities) {
        return grantedAuthorities.contains(PLACE_READ_ALL);
    }

    @Override
    protected boolean canUpdateInternal(Set<String> grantedAuthorities) {
        return false;
    }

    @Override
    protected boolean canDeleteInternal(Set<String> grantedAuthorities) {
        return false;
    }
}
