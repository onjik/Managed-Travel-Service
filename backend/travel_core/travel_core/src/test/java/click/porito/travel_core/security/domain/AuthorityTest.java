package click.porito.travel_core.security.domain;

import click.porito.travel_core.global.constant.Domain;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AuthorityTest {

    @Nested
    class parseAuthoritySet {

        private static Stream<Arguments> parseAuthority() {
            return Stream.of(
                    Arguments.of("ROLE_ADMIN", RoleAuthority.class, new RoleAuthority("ADMIN")),
                    Arguments.of("plan:create", PermissionAuthority.class, new PermissionAuthority(Domain.PLAN, Action.CREATE, null)),
                    Arguments.of("plan:read:owned", PermissionAuthority.class, new PermissionAuthority(Domain.PLAN, Action.READ, Scope.OWNED))
            );
        }

        @ParameterizedTest
        @MethodSource("parseAuthority")
        void parseRoleAuthority() {
            //given
            String authorityString = "ROLE_ADMIN";
            //when
            Authority authority = Authority.parseAuthority(authorityString);
            //then
            assertInstanceOf(RoleAuthority.class, authority);
            assertEquals("ADMIN", ((RoleAuthority) authority).role());
        }
    }

}