package click.porito.travel_core.security.policy;

import click.porito.travel_core.security.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface AccessPolicy<T> {
    boolean canCreate(Authentication authentication) throws ResourceNotFoundException;
    boolean canRead(Authentication authentication, String targetId) throws ResourceNotFoundException;
    boolean canRead(Authentication authentication, List<String> targetIds) throws ResourceNotFoundException;
    boolean canRead(Authentication authentication, T target) throws ResourceNotFoundException;
    boolean canReadOwnedBy(Authentication authentication, String ownerId) throws ResourceNotFoundException;
    boolean canUpdate(Authentication authentication, String targetId) throws ResourceNotFoundException;
    boolean canUpdate(Authentication authentication, List<String> targetIds) throws ResourceNotFoundException;
    boolean canUpdate(Authentication authentication, T target) throws ResourceNotFoundException;
    boolean canDelete(Authentication authentication, String targetId) throws ResourceNotFoundException;
    boolean canDelete(Authentication authentication, T target) throws ResourceNotFoundException;
    boolean canDelete(Authentication authentication, List<String> targetIds) throws ResourceNotFoundException;

    default Set<String> getGrantedAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
