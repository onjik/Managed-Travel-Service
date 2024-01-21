package click.porito.travel_core.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SecurityConfigTest {

    @Nested
    class RoleHierarchyTest{
        private static final String hierarchyString = """
                ROLE_ADMIN > ROLE_STAFF
                ROLE_ADMIN > plan:delete:all
                ROLE_STAFF > ROLE_USER
                ROLE_STAFF > plan:read:all
                ROLE_USER > place:read:all
                ROLE_USER > plan:update:owned
                """;

        private static RoleHierarchy roleHierarchy;

        @BeforeAll
        static void setUp() {
            RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
            hierarchy.setHierarchy(hierarchyString);
            roleHierarchy = hierarchy;
        }

        static Stream<Arguments> provideRolesAndExpectedPermissions() {
            return Stream.of(
                    Arguments.of("ROLE_USER", new String[]{"place:read:all", "plan:update:owned"}),
                    Arguments.of("ROLE_STAFF", new String[]{"place:read:all", "plan:update:owned", "plan:read:all"}),
                    Arguments.of("ROLE_ADMIN", new String[]{"place:read:all", "plan:update:owned", "plan:read:all", "plan:delete:all"})
            );
        }

        @ParameterizedTest
        @MethodSource("provideRolesAndExpectedPermissions")
        void roleHierarchy(String role, String... expectedPermissions) {
            //given

            //when
            Set<String> permissions = roleHierarchy.getReachableGrantedAuthorities(Set.of(new SimpleGrantedAuthority(role)))
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            //then
            for (String expectedPermission : expectedPermissions) {
                Assertions.assertTrue(permissions.contains(expectedPermission));
            }
        }

    }

}