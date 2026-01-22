package in.NihalSingh.invoicegeneratorapi.dto;

import in.NihalSingh.invoicegeneratorapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}
