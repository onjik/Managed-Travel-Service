package click.porito.modular_travel.account.internal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.ReflectionUtils;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.IS_NEW_ACCOUNT;


class CustomOidcUserServiceTest {
    private CustomOidcUserService customOidcUserService;

    private AccountRepository accountRepository;
    private OidcUserRequest oidcUserRequest;
    private OidcUserService oidcUserService;
    private OidcUser oidcUser;

    private Account testUser;
    private String testEmail;

    @BeforeEach
    void setup() throws Exception {
        testEmail = "test@test.com";
        testUser = Account.builder(testEmail, Account.Role.ROLE_USER)
                .name("test")
                .gender(Account.Gender.MALE)
                .build();
        Field userId = Account.class.getDeclaredField("userId");
        ReflectionUtils.makeAccessible(userId);
        ReflectionUtils.setField(userId, testUser, 1L);


        accountRepository = mock(AccountRepository.class);
        oidcUserRequest = mock(OidcUserRequest.class);
        oidcUserService = mock(OidcUserService.class);
        OidcIdToken oidcIdToken = new OidcIdToken("testToken",
                Instant.now(),
                Instant.now().plusSeconds(110000),
                Map.of("email", testEmail,
                        "sub", UUID.randomUUID().toString()));
        oidcUser = new DefaultOidcUser(List.of(()->"ROLE_TEST"),oidcIdToken);

        customOidcUserService = new CustomOidcUserService(accountRepository);
        customOidcUserService.setOidcUserService(oidcUserService);

    }

    @Test
    @DisplayName("이미 저장된 유저의 경우, 저장된 유저를 리턴한다.")
    void loadUser() {
        //given
        when(oidcUserService.loadUser(any())).thenReturn(oidcUser);
        when(accountRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        //when
        OidcUser result = customOidcUserService.loadUser(oidcUserRequest);
        //then
        assertEquals(result.getEmail(),testEmail);
        boolean attribute = (boolean) result.getAttribute(IS_NEW_ACCOUNT);
        assertFalse(attribute);
    }

    @Test
    @DisplayName("저장되지 않은 유저의 경우, 저장 후 리턴한다.")
    void autoJoinWithRoleIncomplete() throws NoSuchFieldException {
        //given
        when(oidcUserService.loadUser(any())).thenReturn(oidcUser);
        when(accountRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenReturn(testUser);

        //when
        OidcUser result = customOidcUserService.loadUser(oidcUserRequest);

        //then
        verify(accountRepository, times(1)).save(
                any());
        assertEquals(result.getEmail(),testEmail);
        boolean attribute = (boolean) result.getAttribute(IS_NEW_ACCOUNT);
        assertTrue(attribute);
    }
}