package click.porito.modular_travel.security.filter;

import click.porito.modular_travel.account.AccountInternalApi;
import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.security.exception.OidcInsufficientUserInfoException;
import click.porito.modular_travel.account.model.Account;
import click.porito.modular_travel.account.Gender;
import click.porito.modular_travel.security.component.OidcLoginFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
class RegisterAuthenticationFilterTest {



    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    Validator validator;

    AccountInternalApi accountApi;

    RegisterAuthenticationFilter registerFilter;

    static ValidatorFactory validatorFactory;
    @BeforeAll
    static void beforeAll(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void afterAll(){
        validatorFactory.close();
    }

    @BeforeEach
    void beforeEach(){
        accountApi = spy(AccountInternalApi.class);
        validator = validatorFactory.getValidator();
        registerFilter = spy(new RegisterAuthenticationFilter(objectMapper, validator, accountApi));
    }



    @Nested
    @DisplayName("attemptAuthentication 메서드")
    class attemptAuthentication{

        @Test
        @DisplayName("세션에 저장된 OidcInsufficientUserInfoException 이 없으면 UsernameNotFoundException 을 던진다.")
        void noOidcInsufficientUserInfoException() throws Exception {
            //given
            var request = new MockHttpServletRequest();
            var response = new MockHttpServletResponse();
            MockHttpSession session = new MockHttpSession(); //empty session
            request.setSession(session);

            Executable executable = () -> registerFilter.attemptAuthentication(request, response);

            //when
            assertThrows(UsernameNotFoundException.class, executable);

        }

        @Test
        @DisplayName("바디가 주어지지 않으면, AuthenticationCredentialsNotFoundException 을 던진다.")
        void noBodyRequest() throws Exception {
            //given
            OidcInsufficientUserInfoException exception = createOidcInsufficientUserInfoException();
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY, exception);

            var request = new MockHttpServletRequest();
            request.setSession(session);
            var response = new MockHttpServletResponse();

            //when
            Executable executable = () -> registerFilter.attemptAuthentication(request, response);

            //then
            assertThrows(AuthenticationCredentialsNotFoundException.class, executable);

        }

        @Test
        @DisplayName("형식이 맞지 않게 바디를 주면, AuthenticationCredentialsNotFoundException 을 던진다.")
        void invalidBodyRequest() {
            //given
            OidcInsufficientUserInfoException exception = createOidcInsufficientUserInfoException();
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY, exception);
            String invalidBody = "{\"gender\":\"not-male\",\"email\":\"not-email-form\"}";
            byte[] bytes = invalidBody.getBytes();

            var request = new MockHttpServletRequest();
            request.setSession(session);
            request.setContent(bytes);
            request.setContentType("application/json");
            var response = new MockHttpServletResponse();

            //when
            Executable executable = () -> registerFilter.attemptAuthentication(request, response);

            //then
            assertThrows(AuthenticationCredentialsNotFoundException.class, executable);
        }

        @Test
        @DisplayName("유저가 주어진 필드를 부족하게 입력하면, OidcInsufficientUserInfoException 을 던진다.")
        void insufficientInfoRequest() throws Exception {
            //given
            OidcUserRequest userRequest = mock(OidcUserRequest.class);
            AccountRegisterDTO onlyName = AccountRegisterDTO.builder().name("onlyName").build();
            AccountRegisterDTO copy = new AccountRegisterDTO(onlyName);//copy
            var violations = validator.validate(onlyName);
            var exception = new OidcInsufficientUserInfoException(userRequest, onlyName, violations);

            MockHttpSession session = new MockHttpSession();
            session.setAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY, exception);

            AccountRegisterDTO insufficientForm = AccountRegisterDTO.builder().gender(Gender.MALE).build();
            byte[] bytes = objectMapper.writeValueAsString(insufficientForm).getBytes();


            var request = new MockHttpServletRequest();
            request.setSession(session);
            request.setContent(bytes);
            request.setContentType("application/json");
            var response = new MockHttpServletResponse();

            //when
            Executable executable = () -> registerFilter.attemptAuthentication(request, response);

            //then
            assertThrows(OidcInsufficientUserInfoException.class, executable);

            //이 때 기존 세션에 있던 값은 변하면 안된다.
            assertEquals(onlyName, copy);


        }

        @Test
        @DisplayName("유저가 주어진 필드를 모두 입력하면, OAuth2AuthenticationToken 을 리턴한다.")
        void properRequest() throws ServletException, IOException {
            //given
            OidcUserRequest userRequest = mock(OidcUserRequest.class);
            AccountRegisterDTO onlyName = AccountRegisterDTO.builder().name("onlyName").build();
            var violations = validator.validate(onlyName);
            var exception = new OidcInsufficientUserInfoException(userRequest, onlyName, violations);

            MockHttpSession session = new MockHttpSession();
            session.setAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY, exception);
            session = spy(session);

            AccountRegisterDTO insufficientForm = AccountRegisterDTO.builder()
                    .email("email@email.com")
                    .birthDate(LocalDate.of(2000, 1, 1))
                    .gender(Gender.MALE)
                    .build();
            byte[] bytes = objectMapper.writeValueAsString(insufficientForm).getBytes();


            var request = new MockHttpServletRequest();
            request.setSession(session);
            request.setContent(bytes);
            request.setContentType("application/json");
            var response = new MockHttpServletResponse();

            //mock
            ClientRegistration registration = mock(ClientRegistration.class);
            when(userRequest.getClientRegistration()).thenReturn(registration);
            when(registration.getRegistrationId()).thenReturn("google");
            Account account = mock(Account.class);
            OidcUser oidcUser = mock(OidcUser.class);
            when(accountApi.registerAccount(any())).thenReturn(account);
            when(account.toOidcUser(userRequest.getIdToken())).thenReturn(oidcUser);
            when(oidcUser.getAuthorities()).thenReturn(Collections.emptyList());
            doNothing().when(registerFilter).saveAuthorizedClient(any(), any(), any(), any());



            //when
            Authentication authentication = registerFilter.attemptAuthentication(request, response);

            //then
            assertNotNull(authentication);
            verify(session, times(1)).removeAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY);
            verify(accountApi, times(1)).registerAccount(any());
        }


    }

    static OidcInsufficientUserInfoException createOidcInsufficientUserInfoException(){
        return new OidcInsufficientUserInfoException(
                mock(OidcUserRequest.class),
                mock(AccountRegisterDTO.class),
                mock(Set.class)
        );
    }

}