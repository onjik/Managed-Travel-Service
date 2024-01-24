package click.porito.account.photo.api.adapter;

import click.porito.account.photo.api.application.ImageApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageApi imageApi;

    @GetMapping("/accounts/{accountId}/profile/image/signed-put-url")
    public String getSignedPutUrl(
            @PathVariable(name = "accountId", required = true) String accountId,
            @RequestParam(name = "filename", required = true) String filename) {
        return imageApi.createImgPutUri(accountId,filename).toString();
    }

    @DeleteMapping("/account/{accountId}/profile/image")
    public ResponseEntity<Void> deleteProfileImage(
            @PathVariable(name = "accountId", required = true) String accountId
    ) {
        imageApi.deleteImg(accountId);
        return ResponseEntity.noContent().build();
    }
}
