package click.porito.modular_travel.account.internal.controller;

import click.porito.modular_travel.account.internal.dto.view.SimpleProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import click.porito.modular_travel.account.internal.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{userId}/profile")
    public SimpleProfile getProfile(@PathVariable("userId") Long userId) {
        return userService.getProfile(userId);
    }
}
