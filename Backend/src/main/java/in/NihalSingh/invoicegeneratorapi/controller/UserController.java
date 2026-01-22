package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.dto.UpdateUserRequest;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET LOGGED-IN USER
    @GetMapping("/me")
    public User getProfile(Authentication auth) {
        return userService.getByEmail(auth.getName());
    }

    // UPDATE PROFILE
    @PutMapping("/update")
    public User updateProfile(
            @RequestBody UpdateUserRequest request,
            Authentication auth
    ) {
        return userService.updateUser(auth.getName(), request);
    }
}
