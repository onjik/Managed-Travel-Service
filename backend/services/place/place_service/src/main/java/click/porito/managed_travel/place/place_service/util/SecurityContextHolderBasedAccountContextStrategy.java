package click.porito.managed_travel.place.place_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SecurityContextHolderBasedAccountContextStrategy implements AccountContextStrategy {
    @Override
    public Optional<Long> getAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (!authentication.isAuthenticated()){
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof Long) {
            return Optional.of((Long) authentication.getPrincipal());
        } else if (authentication.getPrincipal() instanceof String) {
            try {
                return Optional.of(Long.parseLong((String) authentication.getPrincipal()));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<String> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Collections.emptyList();
        }
        if (!authentication.isAuthenticated()){
            return Collections.emptyList();
        }

        if (authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean hasAuthority(String authority) {
        return getAuthorities().contains(authority);
    }
}
