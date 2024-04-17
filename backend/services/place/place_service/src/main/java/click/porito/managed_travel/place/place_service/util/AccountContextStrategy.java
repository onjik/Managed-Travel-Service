package click.porito.managed_travel.place.place_service.util;

import java.util.List;
import java.util.Optional;

public interface AccountContextStrategy {
    Optional<Long> getAccountId();

    List<String> getAuthorities();

    boolean hasAuthority(String authority);
}
