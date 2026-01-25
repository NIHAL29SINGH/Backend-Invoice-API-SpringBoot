package in.NihalSingh.invoicegeneratorapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Embeddable
public class Company {
    private String name;
    private String phone;
    private String address;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String logoBase64;
}