package click.porito.travel_core.security.domain;

import click.porito.travel_core.global.constant.Domain;
import click.porito.travel_core.security.SecurityConfig;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <h1> Authority </h1>
 * Authority 의 경우 두가지 종류가 있다.
 * <ol> 1. {@link RoleAuthority} </ol>
 * <ol> 2. {@link PermissionAuthority} </ol>
 * <p>
 * {@link RoleAuthority} 의 경우 <code>ROLE_</code> prefix 를 가지고 있으며, account service 에서 발급한 사용자의 역할을 나타낸다 <br>
 * {@link PermissionAuthority} 의 경우 <code>domain:action:scope</code> 의 형태를 가지고 있으며, {@link Domain}:{@link Action}:{@link Scope} 을 나타낸다. 보통 소문자로 표기하며,
 * 변환시 대소문자를 무시하도록 구현해야한다. scope 의 경우 없을 수 있다. 즉 plan:create, plan:read:owned 모두 가능하다. <br>
 * {@link PermissionAuthority} 를 정규식으로 표현할 경우 <code>^(?<domain>.+):(?<action>.+)(?::(?<scope>.+))?$</code> 이다. <br>
 * {@link PermissionAuthority} 의 경우 각각의 서버측에서 발급한 것으로, 각각의 서버에서만 유효하다
 * @see RoleHierarchy
 * @see SecurityConfig#roleHierarchy()
 * </p>
 */
public sealed interface Authority permits PermissionAuthority, RoleAuthority {
    final static Pattern PERMISSION_PATTERN = Pattern.compile("^(?<domain>[^:]+):(?<action>[^:]+)(?::(?<scope>[^:]+))?$");
    final static String ROLE_PREFIX = "ROLE_";
    static Set<Authority> parseAuthoritySet(Authentication authentication, RoleHierarchy roleHierarchy) {
        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        if (roleHierarchy != null) {
            userAuthorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
        }
        // map to Authority class
        return userAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(Authority::parseAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * @return null if is not parseable
     */
    static Authority parseAuthority(String authority) {
        Optional<RoleAuthority> roleAuthority = parseRoleAuthority(authority);
        if (roleAuthority.isPresent()) {
            return roleAuthority.get();
        }
        return parsePermissionAuthority(authority)
                .orElse(null);
    }


    private static Optional<RoleAuthority> parseRoleAuthority(String authority) {

        if (authority.startsWith(ROLE_PREFIX)) {
            String role = authority.substring(ROLE_PREFIX.length());
            return Optional.of(new RoleAuthority(role));
        }
        return Optional.empty();
    }

    private static Optional<PermissionAuthority> parsePermissionAuthority(String authority) {
        Matcher matcher = PERMISSION_PATTERN.matcher(authority);
        try {

            if (matcher.matches()) {
                String domainName = matcher.group("domain");
                Domain domain = Domain.valueOf(domainName.toUpperCase());
                String actionName = matcher.group("action");
                Action action = Action.valueOf(actionName.toUpperCase());
                //scope is optional
                String scopeName = matcher.group("scope");
                Scope scope = null;
                if (scopeName != null) {
                    scope = Scope.valueOf(scopeName.toUpperCase());
                }
                return Optional.of(new PermissionAuthority(domain, action, scope));
            }
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
