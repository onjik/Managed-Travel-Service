package click.porito.modular_travel.account.internal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import click.porito.modular_travel.account.internal.dto.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.AccountPrincipal;
import click.porito.modular_travel.account.internal.dto.ProfileResponse;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.service.AccountService;
import click.porito.modular_travel.account.internal.service.ImageObjectService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final ImageObjectService imageService;

    @GetMapping("/account")
    public Account getAccountDetailInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        var principal = AccountPrincipal.from(oidcUser);
        return accountService.getAccountDetailInfo(principal);
    }

    @GetMapping("/account/profile")
    public ProfileResponse getSimpleProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        var principal = AccountPrincipal.from(oidcUser);
        return accountService.getSimpleProfile(principal);
    }

    @GetMapping("/account/profile/image/signed-put-url")
    public String getSignedPutUrl(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestParam(name = "filename", required = true) String filename) {
        var principal = AccountPrincipal.from(oidcUser);
        return imageService.getSignedProfilePutUrl(principal,filename).toString();
    }

    @PatchMapping("/account/profile")
    public ResponseEntity<Void> patchProfileInfo(
            @AuthenticationPrincipal OidcUser oidcUser,
            @Valid @RequestBody AccountPatchRequest body) {
        var principal = AccountPrincipal.from(oidcUser);
        accountService.patchProfileInfo(principal, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account/profile/image")
    public ResponseEntity<Void> deleteProfileImage(@AuthenticationPrincipal OidcUser oidcUser) {
        var principal = AccountPrincipal.from(oidcUser);
        imageService.deleteProfileImage(principal);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal OidcUser oidcUser) {
        var principal = AccountPrincipal.from(oidcUser);
        accountService.deleteAccount(principal);
        return ResponseEntity.noContent().build();
    }

}
