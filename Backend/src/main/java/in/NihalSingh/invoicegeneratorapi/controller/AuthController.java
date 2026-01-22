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

    private final UserService service;
    private final JwtUtil jwt;

    @PostMapping("/request")
    public String request(@RequestBody RegisterRequest req) {
        service.requestRegistration(req);
        return "Token sent to email";
    }

    @PostMapping("/complete")
    public String complete(@RequestBody CompleteRegistrationRequest req) {
        service.completeRegistration(req);
        return "Registration successful";
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {

        User user = service.login(req);

        // ✅ Pass email to JWT
        return jwt.generateToken(user.getEmail());
    }

}


