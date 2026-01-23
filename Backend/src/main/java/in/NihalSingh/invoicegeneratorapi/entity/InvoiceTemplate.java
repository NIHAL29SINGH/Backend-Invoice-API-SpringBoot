package in.NihalSingh.invoicegeneratorapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Template name (Classic, Modern, Professional)
    @Column(nullable = false, unique = true)
    private String name;

    // HTML structure of invoice
    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String htmlTemplate;

    // ✅ NEW: Base64 logo
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String logoBase64;

    private boolean active = true;
}
