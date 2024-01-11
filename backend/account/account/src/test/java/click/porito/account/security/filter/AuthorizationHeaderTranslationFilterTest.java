package click.porito.account.security.filter;

import click.porito.account.security.constant.SecurityConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class AuthorizationHeaderTranslationFilterTest {
    private static AuthorizationHeaderTranslationFilter filter;

    @BeforeAll
    static void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        filter = spy(new AuthorizationHeaderTranslationFilter(objectMapper));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @ParameterizedTest
    @MethodSource("invalidRequests")
    @DisplayName("인증 헤더가 있지만, Id가 숫자가 아닌 경우, 그대로 통과 시킨다.")
    void attemptAuthenticationWithInvalidId(HttpServletRequest request) throws ServletException, IOException {
        //given
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        //when
        filter.doFilter(request, response, filterChain);
        //then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(authentication);
    }

    static private Stream<HttpServletRequest> invalidRequests(){
        Stream<Supplier<HttpServletRequest>> supplierStream = Stream.of(
                //있는데 값이 비어있는 경우
                () -> {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    request.addHeader(SecurityConstant.X_USER_ID, "1");
                    request.addHeader(SecurityConstant.X_USER_ROLES, "");
                    return request;
                },
                //없는 경우
                () -> {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    return request;
                },
                //한개만 있는 경우
                () -> {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    request.addHeader(SecurityConstant.X_USER_ID, "1");
                    return request;
                },
                //없는 경우
                () -> {
                    return new MockHttpServletRequest();
                },
                //있는데 값이 숫자가 아닌 경우
                () -> {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    request.addHeader(SecurityConstant.X_USER_ID, "testId");
                    request.addHeader(SecurityConstant.X_USER_ROLES, "[\"USER\"]");
                    return request;
                }
        );

        return supplierStream.map(Supplier::get);
    }


    @Test
    @DisplayName("역할 헤더에 JSON이 아닌 값이 들어있는 경우, 400")
    void attemptAuthenticationWithInvalidRole() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SecurityConstant.X_USER_ID, "1");
        request.addHeader(SecurityConstant.X_USER_ROLES, "NONE_JSON_FORMAT");
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        //when
        filter.doFilter(request, response, filterChain);
        //then
        verify(filterChain, never()).doFilter(request, response);
        Assertions.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("정상적인 경우, SecurityContext에 Authentication을 넣는다.")
    void attemptAuthenticationWithValidHeader() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "1";
        String[] roles = {"USER"};
        request.addHeader(SecurityConstant.X_USER_ID, "1");
        request.addHeader(SecurityConstant.X_USER_ROLES, "[\"USER\"]");
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        //when
        filter.doFilter(request, response, filterChain);
        //then
        verify(filterChain).doFilter(request, response);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(userId, authentication.getName());
        String[] roleNames = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        Assertions.assertArrayEquals(roles, roleNames);
    }
}