package in.NihalSingh.invoicegeneratorapi.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Shipping {
    private String name;
    private String phone;
    private String address;
}
