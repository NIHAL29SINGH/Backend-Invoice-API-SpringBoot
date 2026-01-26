package in.NihalSingh.invoicegeneratorapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    // ================= ID =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= USER =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // ================= COMPANY =================
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "company_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "company_phone")),
            @AttributeOverride(name = "address", column = @Column(name = "company_address")),
            @AttributeOverride(name = "logoBase64",
                    column = @Column(name = "company_logo", columnDefinition = "LONGTEXT"))
    })
    private Company company;

    // ================= BILLING =================
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "billing_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "billing_phone")),
            @AttributeOverride(name = "address", column = @Column(name = "billing_address"))
    })
    private Billing billing;

    // ================= SHIPPING =================
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "shipping_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "shipping_phone")),
            @AttributeOverride(name = "address", column = @Column(name = "shipping_address"))
    })
    private Shipping shipping;

    // ================= INVOICE DETAILS =================
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "number", column = @Column(name = "invoice_number")),
            @AttributeOverride(name = "date", column = @Column(name = "invoice_date")),
            @AttributeOverride(name = "dueDate", column = @Column(name = "due_date"))
    })
    private InvoiceDetails invoice;

    // ================= ITEMS =================
    @ElementCollection
    @CollectionTable(
            name = "invoice_items",
            joinColumns = @JoinColumn(name = "invoice_id")
    )
    private List<Item> items;

    // ================= TEMPLATE =================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", nullable = true)
    private InvoiceTemplate template;

    // ================= EXTRA =================
    private Double tax;

    private String title;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @CreationTimestamp
    private Instant createdAt;


    private Double subTotal;
    private Double cgst;
    private Double sgst;
    private Double igst;
    private Double totalAmount;

    // ✅ NEW FIELD (SAFE)
    @Column(columnDefinition = "LONGTEXT")
    private String paymentQrBase64;
    // ✅ IMPORTANT: SNAPSHOT OF TEMPLATE HTML
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String templateHtml;
}