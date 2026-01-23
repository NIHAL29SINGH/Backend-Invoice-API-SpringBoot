package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.dto.AdminUserResponse;
import in.NihalSingh.invoicegeneratorapi.entity.Role;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> AdminUserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .role(
                                user.getRole() != null
                                        ? user.getRole().name()
                                        : Role.USER.name()
                        )
                        .enabled(user.isEnabled())
                        .createdAt(user.getCreatedAt())
                        .build()
                )
                .toList();
    }

    public AdminUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AdminUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(
                        user.getRole() != null
                                ? user.getRole().name()
                                : Role.USER.name()
                )
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // ✅ WORKING DELETE
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Let Hibernate handle cascade deletes
        userRepository.delete(user);
    }

}
