package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ Create user if not exists (called during login)
    public User createIfNotExists(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User user = new User();
                    user.setUsername(username);
                    user.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                });
    }

    // ✅ Update profile
    public User updateUser(User updatedUser) {
        User existing = userRepository.findByUsername(updatedUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setEmail(updatedUser.getEmail());
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setPhotoUrl(updatedUser.getPhotoUrl());

        return userRepository.save(existing);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
