package in.NihalSingh.invoicegeneratorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class InvoiceSummaryResponse {

    private Long id;
    private String invoiceNumber;
    private String companyName;
    private String status;
    private Instant createdAt;
}
