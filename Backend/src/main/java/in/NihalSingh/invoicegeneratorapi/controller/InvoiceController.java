package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import in.NihalSingh.invoicegeneratorapi.service.EmailService;
import in.NihalSingh.invoicegeneratorapi.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final EmailService emailService;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
            @RequestBody Invoice invoice,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                invoiceService.saveInvoice(invoice, username)
        );
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Invoice>> getAll(Authentication authentication) {
        return ResponseEntity.ok(
                invoiceService.fetchInvoices(authentication.getName())
        );
    }

    // ✅ GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getOne(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                invoiceService.getInvoiceById(authentication.getName(), id)
        );
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        invoiceService.removeInvoice(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    // ✅ SEND EMAIL
    @PostMapping("/{id}/send")
    public ResponseEntity<String> sendInvoice(
            @PathVariable Long id,
            @RequestParam String email,
            Authentication authentication
    ) {
        Invoice invoice =
                invoiceService.getInvoiceById(authentication.getName(), id);

        emailService.sendInvoiceEmail(email, invoice);
        return ResponseEntity.ok("Invoice sent successfully");
    }
}
