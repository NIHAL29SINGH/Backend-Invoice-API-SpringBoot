package in.NihalSingh.invoicegeneratorapi.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;   // ✅ REQUIRED
    private String firstName;
    private String lastName;
    private String phone;
    private String photoUrl;
}
