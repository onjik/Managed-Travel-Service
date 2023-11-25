package click.porito.modular_travel.gateway.router;

import click.porito.modular_travel.account.AccountService;
import click.porito.modular_travel.account.AccountSummaryDTO;
import click.porito.modular_travel.account.exception.UserNotFoundException;
import click.porito.modular_travel.account.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
class UserRouterTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AccountService accountService;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Nested
    @DisplayName("GET /users/{userId}/profile")
    class getProfile {

        @Test
        @DisplayName("존재하는 회원을 조회하면, 회원 정보를 반환한다.")
        public void getProfile() throws Exception {
            //given
            AccountSummaryDTO dto = AccountSummaryDTO.AccountSummaryDTOImpl.builder()
                    .userId(1L)
                    .name("testname")
                    .profileImgUri("uri")
                    .build();
            when(accountService.retrieveAccountSummaryById(any())).thenReturn(Optional.of(dto));
            //when
            mvc.perform(get("/users/1/profile").with(oidcLogin().authorities(Role.USER)))
            //then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json("{\"userId\":1,\"name\":\"testname\",\"profileImgUri\":\"uri\"}"));
        }

        @Test
        @DisplayName("존재하지 않는 회원을 조회하면, 404를 반환한다.")
        public void getProfile_notFound() throws Exception {
            //given
            when(accountService.retrieveAccountById(any())).thenThrow(new UserNotFoundException());
            //when
            mvc.perform(get("/users/1/profile").with(oidcLogin().authorities(Role.USER)))
            //then
                    .andExpect(status().isNotFound());
        }
    }



}