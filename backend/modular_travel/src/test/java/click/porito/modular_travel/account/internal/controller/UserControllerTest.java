package click.porito.modular_travel.account.internal.controller;

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
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.UserNotFoundException;
import click.porito.modular_travel.account.internal.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

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
            ProfileResponse response = new ProfileResponse(1L, "testname", "uri");
            when(userService.getProfile(any())).thenReturn(response);
            //when
            mvc.perform(get("/users/1/profile").with(oidcLogin().authorities(Account.Role.ROLE_USER)))
            //then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json("{\"userId\":1,\"name\":\"testname\",\"profileImgUri\":\"uri\"}"));
        }

        @Test
        @DisplayName("존재하지 않는 회원을 조회하면, 404를 반환한다.")
        public void getProfile_notFound() throws Exception {
            //given
            when(userService.getProfile(any())).thenThrow(new UserNotFoundException());
            //when
            mvc.perform(get("/users/1/profile").with(oidcLogin().authorities(Account.Role.ROLE_USER)))
            //then
                    .andExpect(status().isNotFound());
        }
    }



}