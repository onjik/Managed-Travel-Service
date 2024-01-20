package click.porito.travel_core.access_controll.domain;

import click.porito.travel_core.global.constant.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record AuthorityMapper(
    Domain domain,
    Action action,
    Scope scope
) {
    public static final Pattern AUTHORITY_PATTERN = Pattern.compile("^(?<domain>[A-z]+):(?<action>[A-z]+):(?<target>[A-z]+)$");

    /**
     * @param authority authority string
     * @return AuthorityMapper or null
     */
    public static AuthorityMapper of(String authority) {
        var matcher = AUTHORITY_PATTERN.matcher(authority);
        if (!matcher.find()) {
            return null;
        }
        var domain = Domain.valueOf(matcher.group("domain").toUpperCase());
        var action = Action.valueOf(matcher.group("action").toUpperCase());
        var target = Scope.valueOf(matcher.group("target").toUpperCase());

        return new AuthorityMapper(domain, action, target);
    }

    /**
     * @param authorities authority strings
     * @return AuthorityMapper list, only valid authorities, exclude ROLE_*
     */
    public static List<AuthorityMapper> list(List<String> authorities) {
        return authorities.stream()
                .map(AuthorityMapper::of)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
