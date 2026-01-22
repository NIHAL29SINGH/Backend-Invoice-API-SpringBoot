package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.dto.CompleteRegistrationRequest;
import in.NihalSingh.invoicegeneratorapi.dto.LoginRequest;
import in.NihalSingh.invoicegeneratorapi.dto.RegisterRequest;
import in.NihalSingh.invoicegeneratorapi.dto.UpdateUserRequest;
import in.NihalSingh.invoicegeneratorapi.entity.RegistrationToken;
import in.NihalSingh.invoicegeneratorapi.entity.Role;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.repository.RegistrationTokenRepository;
import in.NihalSingh.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RegistrationTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ===============================
    // REGISTER (STEP 1)
    // ===============================
    public void requestRegistration(RegisterRequest req) {

        User user = User.builder()
                .email(req.getEmail())
                .firstName(req.getFirstName())
                .username("TEMP_" + UUID.randomUUID())
                .password(passwordEncoder.encode("TEMP_PASS"))
                .role(Role.USER)
                .enabled(false)
                .build();

        userRepo.save(user);

        RegistrationToken token = new RegistrationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiry(LocalDateTime.now().plusMinutes(30));

        tokenRepo.save(token);

        emailService.sendRegistrationMail(user.getEmail(), token.getToken());
    }

    // ===============================
    // COMPLETE REGISTRATION
    // ===============================
    public void completeRegistration(CompleteRegistrationRequest req) {

        RegistrationToken token = tokenRepo.findByToken(req.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        User user = token.getUser();

        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);

        userRepo.save(user);
        tokenRepo.delete(token);

        emailService.sendWelcomeMail(user.getEmail());
        emailService.notifyAdmin(user);
    }

    // ===============================
    // LOGIN
    // ===============================
    public User login(LoginRequest req) {

        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    // ===============================
    // GET USER BY EMAIL (JWT USES EMAIL)
    // ===============================
    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ===============================
    // UPDATE PROFILE
    // ===============================
    public User updateUser(String email, UpdateUserRequest dto) {

        User user = getByEmail(email);

        if (dto.getUsername() != null)
            user.setUsername(dto.getUsername());

        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());

        if (dto.getPhone() != null)
            user.setPhone(dto.getPhone());

        if (dto.getPhotoUrl() != null)
            user.setPhotoUrl(dto.getPhotoUrl());

        return userRepo.save(user);
    }
}
