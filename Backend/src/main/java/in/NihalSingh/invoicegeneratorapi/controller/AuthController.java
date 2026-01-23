package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.dto.*;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.security.JwtUtil;
import in.NihalSingh.invoicegeneratorapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // ============================
    // REGISTER
    // ============================
    @PostMapping("/request")
    public String request(@RequestBody RegisterRequest req) {
        userService.requestRegistration(req);
        return "Token sent to email";
    }

    @PostMapping("/complete")
    public String complete(@RequestBody CompleteRegistrationRequest req) {
        userService.completeRegistration(req);
        return "Registration successful";
    }

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {

        User user = userService.login(req);

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
    }

    // ============================
    // FORGOT PASSWORD
    // ============================
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest req) {
        userService.forgotPassword(req.getEmail());
        return "Password reset token sent to email";
    }

    // ============================
    // RESET PASSWORD
    // ============================
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest req) {
        userService.resetPassword(req.getToken(), req.getNewPassword());
        return "Password updated successfully";
    }
}
