package click.porito.modular_travel.gateway.router;

import click.porito.modular_travel.account.AccountDTO;
import click.porito.modular_travel.account.AccountService;
import click.porito.modular_travel.account.AccountPatchDTO;
import click.porito.modular_travel.account.AccountSummaryDTO;
import click.porito.modular_travel.photo.ImageInternalManagement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountRouter {
    private final AccountService accountApi;
    private final ImageInternalManagement imageService;
    
    @GetMapping("/account")
    public AccountDTO getAccountDetailInfo() {
        return accountApi.retrieveCurrentAccount();
    }

    @GetMapping("/account/profile")
    public AccountSummaryDTO getSimpleProfile() {
        return accountApi.retrieveCurrentAccountSummary();
    }

    @GetMapping("/account/profile/image/signed-put-url")
    public String getSignedPutUrl(
            @RequestParam(name = "filename", required = true) String filename) {
        return imageService.createAccountImgPutUri(filename).toString();
    }

    @PatchMapping("/account/profile")
    public ResponseEntity<Void> patchProfileInfo(
            @Valid @RequestBody AccountPatchDTO body) {
        accountApi.patchProfileInfo(body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account/profile/image")
    public ResponseEntity<Void> deleteProfileImage() {
        imageService.deleteAccountImg();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount() {
        accountApi.deleteCurrentAccount();
        return ResponseEntity.noContent().build();
    }

}
