package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.dto.*;
import in.NihalSingh.invoicegeneratorapi.entity.*;
import in.NihalSingh.invoicegeneratorapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import in.NihalSingh.invoicegeneratorapi.entity.PasswordResetToken;
import in.NihalSingh.invoicegeneratorapi.repository.PasswordResetTokenRepository;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RegistrationTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository resetTokenRepo;


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
    // FETCH USER USING EMAIL (JWT)
    // ===============================
    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ===============================
    // UPDATE USER
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

    // FORGOT PASSWORD
    public void forgotPassword(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiry(LocalDateTime.now().plusMinutes(15));

        resetTokenRepo.save(token);

        emailService.sendPasswordResetMail(email, token.getToken());
    }
    // RESET PASSWORD
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken =
                resetTokenRepo.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepo.save(user);
        resetTokenRepo.delete(resetToken);
    }

}
