package click.porito.modular_travel.account.internal.controller;

import click.porito.modular_travel.account.internal.dto.view.AccountPatchRequest;
import click.porito.modular_travel.account.internal.dto.view.DetailedProfile;
import click.porito.modular_travel.account.internal.dto.view.SimpleProfile;
import click.porito.modular_travel.account.internal.service.AccountService;
import click.porito.modular_travel.account.internal.service.ImageObjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final ImageObjectService imageService;
    
    @GetMapping("/account")
    public DetailedProfile getAccountDetailInfo() {
        return accountService.getDetailedProfile();
    }

    @GetMapping("/account/profile")
    public SimpleProfile getSimpleProfile() {
        return accountService.getSimpleProfile();
    }

    @GetMapping("/account/profile/image/signed-put-url")
    public String getSignedPutUrl(
            @RequestParam(name = "filename", required = true) String filename) {
        return imageService.createAccountImgPutUri(filename).toString();
    }

    @PatchMapping("/account/profile")
    public ResponseEntity<Void> patchProfileInfo(
            @Valid @RequestBody AccountPatchRequest body) {
        accountService.patchProfileInfo(body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account/profile/image")
    public ResponseEntity<Void> deleteProfileImage() {
        imageService.deleteAccountImg();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount() {
        accountService.deleteAccount();
        return ResponseEntity.noContent().build();
    }

}
