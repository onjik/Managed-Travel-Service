package click.porito.modular_travel.security.filter;

import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.account.AccountService;
import click.porito.modular_travel.account.Gender;
import click.porito.modular_travel.account.model.Account;
import click.porito.modular_travel.account.model.Role;
import click.porito.modular_travel.security.component.DefaultLoginFailureHandler;
import click.porito.modular_travel.security.component.DefaultLoginSuccessHandler;
import click.porito.modular_travel.security.constant.SecurityConstant;
import click.porito.modular_travel.security.exception.InsufficientRegisterInfoException;
import click.porito.modular_travel.security.model.SimpleAuthentication;
import click.porito.modular_travel.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class SupplementRegisterAuthenticationFilterTest {
    private static ValidatorFactory validatorFactory;
    private Validator validator;
    private ObjectMapper objectMapper;
    private AccountService accountService;
    private JwtService jwtService;
    private SupplementRegisterAuthenticationFilter filter;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

    @BeforeEach
    void setUp() {
        this.validator = validatorFactory.getValidator();
        this.objectMapper = spy(new ObjectMapper());
        this.accountService = mock(AccountService.class);
        this.jwtService = mock(JwtService.class);
        this.successHandler = mock(DefaultLoginSuccessHandler.class);
        this.failureHandler = mock(DefaultLoginFailureHandler.class);
        this.filter = spy(new SupplementRegisterAuthenticationFilter(validator, objectMapper, accountService, jwtService, failureHandler, successHandler));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("request matcher")
    class RequestMatcherTest {
        @Test
        @DisplayName("request method가 POST이고, request url이 /account/register일 때 인증을 시도한다.")
        void match() throws ServletException, IOException {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain chain = mock(FilterChain.class);
            request.setRequestURI(SecurityConstant.SUPPLEMENT_REGISTER_URI);
            request.setMethod("POST");
            Authentication authentication = new SimpleAuthentication(List.of("USER"), "1");
            doAnswer(invocation -> {
                return authentication;
            }).when(filter).attemptAuthentication(request, response);
            // when
            filter.doFilter(request, response, chain);
            // then
            //TODO 여기부터
            verify(filter, times(1)).attemptAuthentication(request, response);
            verify(successHandler, times(1)).onAuthenticationSuccess(request, response, authentication);
        }

        @ParameterizedTest
        @MethodSource("unmatchedRequest")
        @DisplayName("다른 요청일 때는 인증을 건너 뛴다.")
        void unmatched(HttpServletRequest request) throws ServletException, IOException {
            // given
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain chain = mock(FilterChain.class);
            // when
            Assertions.assertDoesNotThrow(() -> filter.doFilter(request, response, chain));
            // then
            verify(filter, never()).attemptAuthentication(request, response);


        }

        static private Stream<HttpServletRequest> unmatchedRequest() {
            String differentUri = SecurityConstant.SUPPLEMENT_REGISTER_URI + "/different";
            Stream<Supplier<HttpServletRequest>> stream = Stream.of(
                    //uri가 다른 경우
                    () -> {
                        MockHttpServletRequest request = new MockHttpServletRequest();
                        request.setRequestURI("/account/register/different");
                        request.setMethod("POST");
                        return request;
                    },
                    //메서드가 다른경우
                    () -> {
                        MockHttpServletRequest request = new MockHttpServletRequest();
                        request.setRequestURI("/account/register");
                        request.setMethod("GET");
                        return request;
                    },
                    //uri, 메서드 모두 다른 경우
                    () -> {
                        MockHttpServletRequest request = new MockHttpServletRequest();
                        request.setRequestURI("/account/register/different");
                        request.setMethod("GET");
                        return request;
                    }
            );
            return stream.map(Supplier::get);
        }
    }

    @Nested
    @DisplayName("attemptAuthentication")
    class attemptAuthentication {

        @Test
        @DisplayName("필수 정보가 누락되었을 때, InsufficientRegisterInfoException 발생")
        void insufficientInfo() {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            var original = AccountRegisterDTO.builder()
                    .name("name")
                    .build();
            var insufficientSupplement = AccountRegisterDTO.builder()
                    .email("email@email.com")
                    .build();
            doReturn(original).when(filter).parseBody(request);
            doReturn(insufficientSupplement).when(filter).parseCookie(request);
            when(accountService.registerAccount(any())).thenThrow(DataIntegrityViolationException.class);


            // when, then
            Assertions.assertThrows(InsufficientRegisterInfoException.class,
                    () -> filter.attemptAuthentication(request, response));
        }

        @Test
        @DisplayName("유저가 이미 가입되어 있는데, 쿠키를 통해 다시 가입하려고 할 때, InvalidCookieException 발생")
        void invalidCookie() {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            var original = AccountRegisterDTO.builder()
                    .name("name")
                    .email("email@email.com")
                    .build();
            var supplement = AccountRegisterDTO.builder()
                    .gender(Gender.MALE)
                    .birthDate(LocalDate.of(2000, 1, 1))
                    .build();
            doReturn(original).when(filter).parseBody(request);
            doReturn(supplement).when(filter).parseCookie(request);
            doThrow(DataIntegrityViolationException.class).when(accountService).registerAccount(any());


            // when, then
            Assertions.assertThrows(InvalidCookieException.class,
                    () -> filter.attemptAuthentication(request, response));
            verify(filter, times(1)).eraseJwtCookie(response);
        }

        @Test
        @DisplayName("추가 정보 요청이 왔는데, 쿠키에 토큰이 담겨있지 않을때, AuthenticationCredentialsNotFoundException 발생")
        void noCookie() {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            var original = AccountRegisterDTO.builder()
                    .name("name")
                    .email("email@email.com")
                    .build();
            doReturn(original).when(filter).parseBody(request);
            doThrow(AuthenticationCredentialsNotFoundException.class).when(filter).parseCookie(request);


            // when, then
            Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                    () -> filter.attemptAuthentication(request, response));
        }

        @Test
        @DisplayName("쿠키가 유효하고, 추가 정보가 다 제공되고, 아직 가입되지 않은 사용자라면, Authentication을 리턴한다.")
        void registrationSuccess() throws ServletException, IOException {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            var original = AccountRegisterDTO.builder()
                    .name("name")
                    .email("email@email.com")
                    .build();
            var supplement = AccountRegisterDTO.builder()
                    .gender(Gender.MALE)
                    .birthDate(LocalDate.of(2000, 1, 1))
                    .build();
            Account account = Account.builder("email@email.com", Role.USER)
                    .gender(Gender.MALE)
                    .name("name")
                    .birthDate(LocalDate.of(2000, 1, 1))
                    .build();
            account.setUserId(1L);
            doReturn(original).when(filter).parseBody(request);
            doReturn(supplement).when(filter).parseCookie(request);
            doReturn(account).when(accountService).registerAccount(any());


            // when
            Authentication authentication = filter.attemptAuthentication(request, response);

            //then
            Assertions.assertNotNull(authentication);
            Assertions.assertEquals(account.getUserId().toString(), authentication.getName());
            verify(filter, times(1)).eraseJwtCookie(response);
        }
    }


}