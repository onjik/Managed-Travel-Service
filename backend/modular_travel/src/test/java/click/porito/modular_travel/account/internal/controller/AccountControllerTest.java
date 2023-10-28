package click.porito.modular_travel.account.internal.controller;

import click.porito.modular_travel.account.internal.dto.view.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.view.DetailedProfile;
import click.porito.modular_travel.account.internal.dto.view.SimpleProfile;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.exception.InvalidAuthenticationException;
import click.porito.modular_travel.account.internal.service.AccountService;
import click.porito.modular_travel.account.internal.service.ImageObjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    AccountService accountService;

    @SpyBean
    ImageObjectService imageService;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Nested
    @DisplayName("GET /account")
    class getDetailAccount {


        @Test
        @DisplayName("인증이 안된 상태로 호출하면, 401을 반환한다.")
        void getAccountDetailInfoWithoutAuthentication() throws Exception {

            mvc.perform(get("/account"))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("정상적으로 계정 정보를 조회하면, 200과 함께 바디를 반환한다.")
        void getAccountDetailInfo() throws Exception {
            //given
            DetailedProfile detailedProfile = new DetailedProfile(1L, "testName", "email@email.com", Instant.now(), Account.Gender.MALE, LocalDate.now(), null);
            doReturn(detailedProfile).when(accountService).getDetailedProfile();

            //when
            MvcResult mvcResult = mvc.perform(get("/account"))
                    .andExpect(status().isOk())
                    .andReturn();

            //then
            verify(accountService, times(1)).getDetailedProfile();
            String content = mvcResult.getResponse().getContentAsString();
            DetailedProfile profile = objectMapper.readValue(content, DetailedProfile.class);
            assertEquals(detailedProfile, profile);

        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("계정 정보를 조회하다 인증 정보에 문제가 생기면, 401을 반환한다.")
        void getAccountDetailInfoWithInvalidAuthentication() throws Exception {
            //given
            doThrow(new InvalidAuthenticationException()).when(accountService).getDetailedProfile();

            //when
            mvc.perform(get("/account"))
                    //then
                    .andExpect(status().isUnauthorized());

            verify(accountService, times(1)).getDetailedProfile();
        }
    }

    @Nested
    @DisplayName("GET /account/profile")
    class getDetailedAccountInfo{

        @Test
        @DisplayName("인증이 안된 상태로 호출하면, 401을 반환한다.")
        void getAccountDetailInfoWithoutAuthentication() throws Exception {

            mvc.perform(get("/account/profile"))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("정상적으로 간단한 프로필 정보를 조회하면, 200과 함께 바디를 반환한다.")
        void getSimpleProfile() throws Exception {
            //given
            SimpleProfile profile = new SimpleProfile(1L, "test", null);
            doReturn(profile).when(accountService).getSimpleProfile();

            //when
            MvcResult mvcResult = mvc.perform(get("/account/profile"))
                    //then
                    .andExpect(status().isOk())
                    .andReturn();

            //then
            verify(accountService, times(1)).getSimpleProfile();
            String content = mvcResult.getResponse().getContentAsString();
            SimpleProfile result = objectMapper.readValue(content, SimpleProfile.class);
            assertEquals(profile, result);


        }

        @Test
        @DisplayName("간단한 프로필 정보를 조회하다 인증 정보에 문제가 생기면, 401을 반환한다.")
        void getSimpleProfileWithInvalidAuthentication() throws Exception {
            //given
            doThrow(new InvalidAuthenticationException()).when(accountService).getSimpleProfile();

            //when
            mvc.perform(get("/account/profile"))
                    //then
                    .andExpect(status().isUnauthorized());

        }
    }

    @Nested
    @DisplayName("GET /account/profile/image/signed-put-url")
    class getSignedPutUrl{

        @Test
        @DisplayName("인증이 안된 상태로 호출하면, 401을 반환한다.")
        void getAccountDetailInfoWithoutAuthentication() throws Exception {

            mvc.perform(get("/account/profile/image/signed-put-url"))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("허용된 파일 형식(jpg,jpeg,png)와 함께 요청하면, 200과 함께 바디를 반환한다.")
        void getSignedPutUrl() throws Exception {
            //given
            String filename = "test.jpg";
            String signedPutUrl = "https://test.com";
            doReturn(new URL(signedPutUrl)).when(imageService).createAccountImgPutUri(anyString());

            //when
            MvcResult mvcResult = mvc.perform(get("/account/profile/image/signed-put-url")
                            .queryParam("filename", filename))
                    //then
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn();


            //then
            verify(imageService, times(1)).createAccountImgPutUri(filename);
            String content = mvcResult.getResponse().getContentAsString(); //plain text
            assertEquals(signedPutUrl, content);

        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("쿼리 스트링을 전달하지 않으면 400을 반환한다.")
        void missingQueryString() throws Exception {
            //given
            //when
            mvc.perform(get("/account/profile/image/signed-put-url"))
                    //then
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("지원하지 않는 파일 확장자를 전달하면 400을 반환한다.")
        void invalidFileExtension() throws Exception {
            //given
            String filename = "test.txt";
            //when
            mvc.perform(get("/account/profile/image/signed-put-url")
                            .queryParam("filename", filename))
                    //then
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("DELETE /account/profile/image")
    class deleteProfileImg{



        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("정상적으로 프로필 이미지를 삭제하면, 204를 반환한다.")
        void deleteProfileImg() throws Exception {
            //given
            doNothing().when(imageService).deleteAccountImg();
            //when
            mvc.perform(delete("/account/profile/image"))
                    //then
                    .andExpect(status().isNoContent());
            verify(imageService, times(1)).deleteAccountImg();
        }

    }

    @Nested
    @DisplayName("DELETE /account")
    class deleteAccount {

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("정상적으로 계정을 삭제하면, 204를 반환한다.")
        void deleteAccount() throws Exception {
            //given
            doNothing().when(accountService).deleteAccount();
            //when
            mvc.perform(delete("/account"))
                    //then
                    .andExpect(status().isNoContent());
            //then
            verify(accountService, times(1)).deleteAccount();
        }

    }

    @Nested
    @DisplayName("PATCH /account/profile")
    class patchProfileInfo {


        @Test
        @DisplayName("인증이 안된 상태로 호출하면, 401을 반환한다.")
        void patchProfileInfoWithoutAuthentication() throws Exception {
            //given
            AccountPatchRequest patchRequest = new AccountPatchRequest();
            patchRequest.setBirthDate(LocalDate.of(1999, 1, 1));
            String json = objectMapper.writeValueAsString(patchRequest);
            //when
            mvc.perform(patch("/account/profile")
                            .contentType("application/json")
                            .content(json))
                    .andExpect(status().isUnauthorized());

            //then
            verify(accountService, times(0)).patchProfileInfo(any());

        }

        @Test
        @DisplayName("형식이 맞지 않는 경우, 400을 반환한다.")
        void patchProfileInfoWithInvalidFormat() throws Exception {
            //given
            AccountPatchRequest patchRequest = new AccountPatchRequest();
            patchRequest.setBirthDate(LocalDate.now().plusYears(5));
            patchRequest.setName("toLong".repeat(100));
            String json = objectMapper.writeValueAsString(patchRequest);
            //when
            MvcResult mvcResult = mvc.perform(patch("/account/profile")
                            .contentType("application/json")
                            .content(json))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            //then
            verify(accountService, times(0)).patchProfileInfo(any());

        }

        @Test
        @WithMockUser(username = "1", roles = {"USER"})
        @DisplayName("정상적으로 프로필 정보를 수정하면, 204를 반환한다.")
        void patchProfile() throws Exception {
            //given
            doNothing().when(accountService).patchProfileInfo(any());
            AccountPatchRequest patchRequest = new AccountPatchRequest();
            patchRequest.setBirthDate(LocalDate.of(1999, 1, 1));
            String json = objectMapper.writeValueAsString(patchRequest);
            //when
            mvc.perform(patch("/account/profile")
                            .contentType("application/json")
                            .content(json))
                    .andExpect(status().isNoContent());

            //then
            verify(accountService, times(1)).patchProfileInfo(any());

        }
    }
}