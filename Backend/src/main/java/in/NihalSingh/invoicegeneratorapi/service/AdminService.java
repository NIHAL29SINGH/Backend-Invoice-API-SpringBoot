package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.dto.AdminUserResponse;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.repository.InvoiceRepository;
import in.NihalSingh.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    // ✅ GET ALL USERS WITH INVOICE STATS
    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhone(),
                        user.isEnabled(),
                        user.getRole().name(),

                        invoiceRepository.countByUserId(user.getId()),        // ✅ REQUIRED
                        invoiceRepository.findUsedTemplateNames(user.getId()),

                        user.getCreatedAt()
                ))
                .toList();
    }

    // ✅ GET USER BY ID
    public AdminUserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "User not found"));

        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.isEnabled(),
                user.getRole().name(),

                invoiceRepository.countByUserId(user.getId()),
                invoiceRepository.findUsedTemplateNames(user.getId()),

                user.getCreatedAt()
        );
    }

    // ✅ DELETE USER
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "User not found"));

        userRepository.delete(user);
    }
}
