package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.security.JwtUtil;
import in.NihalSingh.invoicegeneratorapi.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    // ==============================
    // LOGIN / REGISTER
    // ==============================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        // Save user if not exists
        User user = userService.createIfNotExists(request.getUsername());

        // Generate JWT
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(
                new AuthResponse(token, user)
        );
    }

    // ==============================
    // DTOs
    // ==============================
    @Data
    public static class LoginRequest {
        private String username;
    }

    @Data
    public static class AuthResponse {
        private final String token;
        private final User user;
    }
}
