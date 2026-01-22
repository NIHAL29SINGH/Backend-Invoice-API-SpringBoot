package in.NihalSingh.invoicegeneratorapi.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Data
@Embeddable
public class InvoiceDetails {
    private String number;
    private LocalDate date;
    private LocalDate dueDate;
}
