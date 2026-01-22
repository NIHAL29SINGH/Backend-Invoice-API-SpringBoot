package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ============================
    // UPDATE LOGGED-IN USER
    // ============================
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User user, Authentication authentication) {

        String username = authentication.getName();

        // Force correct user
        user.setUsername(username);

        return userService.updateUser(user);
    }

    // ============================
    // GET LOGGED-IN USER
    // ============================
    @GetMapping("/me")
    public User getLoggedInUser(Authentication authentication) {
        return userService.getByUsername(authentication.getName());
    }
}
