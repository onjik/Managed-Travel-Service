package click.porito.travel_core.access_controll.operation;

import click.porito.travel_core.place.PlaceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface AccessPolicy<T> {
    boolean canCreate(Authentication authentication) throws PlaceNotFoundException;
    boolean canRead(Authentication authentication, String targetId) throws PlaceNotFoundException;
    boolean canRead(Authentication authentication, List<String> targetIds) throws PlaceNotFoundException;
    boolean canReadOwnedBy(Authentication authentication, String ownerId) throws PlaceNotFoundException;
    boolean canUpdate(Authentication authentication, String targetId) throws PlaceNotFoundException;
    boolean canUpdate(Authentication authentication, List<String> targetIds) throws PlaceNotFoundException;
    boolean canUpdate(Authentication authentication, T target) throws PlaceNotFoundException;
    boolean canDelete(Authentication authentication, String targetId) throws PlaceNotFoundException;
    boolean canDelete(Authentication authentication, T target) throws PlaceNotFoundException;
    boolean canDelete(Authentication authentication, List<String> targetIds) throws PlaceNotFoundException;

    default Set<String> getGrantedAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
