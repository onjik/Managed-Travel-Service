package click.porito.modular_travel.account.internal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import click.porito.modular_travel.account.internal.dto.AccountPrincipal;
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.ImageTypeNotSupportedException;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.internal.service.AccountService;
import click.porito.modular_travel.account.internal.service.ImageObjectService;

import java.net.URL;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.IS_NEW_ACCOUNT;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.USER_ID;

@ActiveProfiles("test")
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    AccountService accountService;

    @MockBean
    ImageObjectService imageService;

    private MockMvc mvc;
    SecurityMockMvcRequestPostProcessors.OidcLoginRequestPostProcessor oidcAuthority; //인증 정보

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        oidcAuthority = oidcLogin()
                .userInfoToken(builder -> {
                    builder.claims(map -> {
                        map.put(USER_ID, 1L);
                        map.put(IS_NEW_ACCOUNT, true);
                    });
                })
                .authorities(Account.Role.ROLE_USER);
    }

    @Nested
    @DisplayName("GET /account")
    class getDetailAccount {

        @Test
        @DisplayName("정상적으로 계정 정보를 조회하면, 200과 함께 바디를 반환한다.")
        void getAccountDetailInfo() throws Exception {
            //given
            Account testUser = Account.builder("test@test.com", Account.Role.ROLE_USER)
                    .name("test")
                    .gender(Account.Gender.MALE)
                    .build();
            when(accountService.getAccountDetailInfo(any(AccountPrincipal.class))).thenReturn(testUser);
            //when
            mvc.perform(get("/account").with(oidcAuthority))
            //then
                    .andExpect(status().isOk());
            verify(accountService, times(1)).getAccountDetailInfo(any(AccountPrincipal.class));
        }

        @Test
        @DisplayName("계정 정보를 조회하다 인증 정보에 문제가 생기면, 401을 반환한다.")
        void getAccountDetailInfoWithInvalidAuthentication() throws Exception {
            //given
            when(accountService.getAccountDetailInfo(any(AccountPrincipal.class))).thenThrow(new InvalidAuthenticationException());

            //when
            mvc.perform(get("/account").with(oidcAuthority))
            //then
                    .andExpect(status().isUnauthorized());
            verify(accountService, times(1)).getAccountDetailInfo(any(AccountPrincipal.class));
        }
    }

    @Nested
    @DisplayName("GET /account/profile")
    class getDetailedAccountInfo{

        @Test
        @DisplayName("정상적으로 간단한 프로필 정보를 조회하면, 200과 함께 바디를 반환한다.")
        void getSimpleProfile() throws Exception {
            //given
            Account testUser = Account.builder("test@test.com", Account.Role.ROLE_USER)
                    .name("test")
                    .gender(Account.Gender.MALE)
                    .build();
            when(accountService.getSimpleProfile(any(AccountPrincipal.class))).thenReturn(ProfileResponse.from(testUser));
            //when
            mvc.perform(get("/account/profile").with(oidcAuthority))
                    //then
                    .andExpect(status().isOk());
            verify(accountService, times(1)).getSimpleProfile(any(AccountPrincipal.class));
        }

        @Test
        @DisplayName("간단한 프로필 정보를 조회하다 인증 정보에 문제가 생기면, 401을 반환한다.")
        void getSimpleProfileWithInvalidAuthentication() throws Exception {
            //given
            when(accountService.getSimpleProfile(any(AccountPrincipal.class))).thenThrow(new InvalidAuthenticationException());
            //when
            mvc.perform(get("/account/profile").with(oidcAuthority))
                    //then
                    .andExpect(status().isUnauthorized());
            verify(accountService, times(1)).getSimpleProfile(any(AccountPrincipal.class));
        }
    }

    @Nested
    @DisplayName("GET /account/profile/image/signed-put-url")
    class getSignedPutUrl{
        @Test
        @DisplayName("정상적으로 서명된 PUT URL을 반환하면, 200과 함께 바디를 반환한다.")
        void getSignedPutUrl() throws Exception {
            //given
            when(imageService.getSignedProfilePutUrl(any(AccountPrincipal.class), anyString())).thenReturn(new URL("https://test.com"));
            //when
            mvc.perform(get("/account/profile/image/signed-put-url")
                            .queryParam("filename", "test.jpg")
                            .with(oidcAuthority))
                    //then
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
        }

        @Test
        @DisplayName("쿼리 스트링을 전달하지 않으면 400을 반환한다.")
        void missingQueryString() throws Exception {
            //given
            //when
            mvc.perform(get("/account/profile/image/signed-put-url")
                            .with(oidcAuthority))
                    //then
                    .andExpect(status().isBadRequest())
                    .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
        }

        @Test
        @DisplayName("지원하지 않는 파일 확장자를 전달하면 400을 반환한다.")
        void invalidFileExtension() throws Exception {
            //given
            when(imageService.getSignedProfilePutUrl(any(AccountPrincipal.class), anyString())).thenThrow(new ImageTypeNotSupportedException("unsupportedMimeType"));

            //when
            mvc.perform(get("/account/profile/image/signed-put-url")
                            .queryParam("filename", "test.unknown")
                            .with(oidcAuthority))
                    //then
                    .andExpect(status().isBadRequest())
                    .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
        }

    }

    @Nested
    @DisplayName("DELETE /account/profile/image")
    class deleteProfileImg{

        @Test
        @DisplayName("정상적으로 프로필 이미지를 삭제하면, 204를 반환한다.")
        void deleteProfileImg() throws Exception {
            //given
            //when
            mvc.perform(delete("/account/profile/image")
                            .with(oidcAuthority))
                    //then
                    .andExpect(status().isNoContent());
            verify(imageService, times(1)).deleteProfileImage(any());
        }

    }

    @Nested
    @DisplayName("DELETE /account")
    class deleteAccount {

        @Test
        @DisplayName("정상적으로 계정을 삭제하면, 204를 반환한다.")
        void deleteAccount() throws Exception {
            //given
            //when
            mvc.perform(delete("/account")
                            .with(oidcAuthority))
                    //then
                    .andExpect(status().isNoContent());
            verify(accountService, times(1)).deleteAccount(any(AccountPrincipal.class));
        }

    }

    @Nested
    @DisplayName("PATCH /account/profile")
    class patchProfileInfo {

        @Test
        @DisplayName("정상적으로 프로필 정보를 수정하면, 204를 반환한다.")
        void patchProfile() throws Exception {
            //given
//            String jsonBody = "{\"name\":\"test\",\"gender\":null,\"birthDate\":null,\"locale\":null}";
            String jsonBody2 = "{\"name\":\"test\"}";
            //when
            mvc.perform(patch("/account/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody2)
                    .with(oidcAuthority))
                    .andExpect(status().isNoContent());
            verify(accountService, times(1)).patchProfileInfo(any(AccountPrincipal.class), any());
        }

        @Test
        @DisplayName("제한 사항을 지키지 않으면, 400을 반환한다.")
        void validationFail() throws Exception {
            //given
            String invalidName = "{\"name\":\"\"}";
            mvc.perform(patch("/account/profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidName)
                            .with(oidcAuthority))
                    .andExpect(status().isBadRequest());

            String invalidGender = "{\"gender\":\"UNKNOWN\"}";
            mvc.perform(patch("/account/profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidGender)
                            .with(oidcAuthority))
                    .andExpect(status().isBadRequest());

            LocalDate future = LocalDate.now().plusDays(1);

            String invalidBirthDate = "{\"birthDate\":\"" + future + "\"}";
            mvc.perform(patch("/account/profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBirthDate)
                            .with(oidcAuthority))
                    .andExpect(status().isBadRequest());
        }
    }

}