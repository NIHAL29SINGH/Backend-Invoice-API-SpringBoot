package in.NihalSingh.invoicegeneratorapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // ================= COMPANY =================
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "company_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "company_phone")),
            @AttributeOverride(name = "address", column = @Column(name = "company_address"))
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
            @AttributeOverride(name = "number", column = @Column(name = "number")),
            @AttributeOverride(name = "date", column = @Column(name = "date")),
            @AttributeOverride(name = "dueDate", column = @Column(name = "due_date"))
    })
    private InvoiceDetails invoice;

    // ================= ITEMS =================
    @ElementCollection
    @CollectionTable(name = "invoice_items", joinColumns = @JoinColumn(name = "invoice_id"))
    private List<Item> items;

    private Double tax;
    private String template;
    private String title;

    private Instant createdAt;
}
