package in.NihalSingh.invoicegeneratorapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AdminUserResponse(

        Long id,
        String email,
        String username,
        String firstName,
        String lastName,
        String phone,
        boolean enabled,
        String role,

        Long invoiceCount,                  // ✅ ADD THIS
        List<String> usedTemplates,         // ✅ Already exists

        LocalDateTime createdAt
) {}
