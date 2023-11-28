package click.porito.modular_travel.security.component;

import click.porito.modular_travel.account.AccountService;
import click.porito.modular_travel.account.model.Account;
import click.porito.modular_travel.account.model.Role;
import click.porito.modular_travel.security.event.AuthenticationSuccessEvent;
import click.porito.modular_travel.security.event.SecurityTopics;
import click.porito.modular_travel.security.exception.OidcUnexpectedServerError;
import click.porito.modular_travel.security.model.SimpleAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultLoginSuccessHandlerTest {
    private AccountService accountService;
    private ObjectMapper objectMapper;
    private KafkaTemplate<String,Object> kafkaTemplate;
    private DefaultLoginSuccessHandler defaultLoginSuccessHandler;

    @BeforeEach
    void setUp() {
        this.accountService = mock(AccountService.class);
        this.objectMapper = new ObjectMapper();
        this.kafkaTemplate = mock(KafkaTemplate.class);
        defaultLoginSuccessHandler = new DefaultLoginSuccessHandler(accountService, objectMapper, kafkaTemplate);
    }

    @Test
    @DisplayName("로그인 성공시, 200 응답을 보내고 카프카로 성공 이벤트 발송")
    void onAuthenticationSuccess() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        SimpleAuthentication authentication = new SimpleAuthentication(List.of("USER"), "1");
        Account account = Account.builder("email@email.com", Role.USER)
                .name("name")
                .birthDate(LocalDate.of(1999, 1, 1))
                .build();
        doReturn(Optional.of(account)).when(accountService).retrieveAccountById(any());
        //when
        defaultLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        //then
        verify(kafkaTemplate).send(eq(SecurityTopics.AUTHENTICATION_SUCCESS_0), any(AuthenticationSuccessEvent.class));
    }

    @Test
    @DisplayName("로그인 성공 했는데, DB에서 계정을 못찾으면, OidcUnexpectedServerError를 던진다.")
    void onAuthenticationSuccess_throwOidcUnexpectedServerError() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        SimpleAuthentication authentication = new SimpleAuthentication(List.of("USER"), "1");
        doReturn(Optional.empty()).when(accountService).retrieveAccountById(any());
        //when
        //then
        Assertions.assertThrows(OidcUnexpectedServerError.class, () -> defaultLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication));
    }


}