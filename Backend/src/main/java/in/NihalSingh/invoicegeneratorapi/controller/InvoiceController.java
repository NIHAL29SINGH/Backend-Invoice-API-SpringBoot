package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.dto.InvoiceSummaryResponse;
import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import in.NihalSingh.invoicegeneratorapi.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // ================= CREATE =================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Invoice> createInvoice(
            @RequestPart("invoice") String invoiceJson,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                invoiceService.createInvoice(invoiceJson, logo, auth.getName())
        );
    }

    // ================= LIST =================
    @GetMapping
    public ResponseEntity<List<InvoiceSummaryResponse>> getAll(Authentication auth) {
        return ResponseEntity.ok(
                invoiceService.getAllInvoices(auth.getName())
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            Authentication auth
    ) {
        invoiceService.deleteInvoice(id, auth.getName());
        return ResponseEntity.ok("Invoice deleted successfully");
    }

    // ================= PREVIEW =================
    @GetMapping("/{id}/template/{templateId}/preview")
    public ResponseEntity<byte[]> preview(
            @PathVariable Long id,
            @PathVariable Long templateId,
            Authentication auth
    ) {
        byte[] pdf = invoiceService.previewInvoice(id, templateId, auth.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf")
                .body(pdf);
    }

    // ================= DOWNLOAD =================
    @GetMapping("/{id}/template/{templateId}/download")
    public ResponseEntity<byte[]> download(
            @PathVariable Long id,
            @PathVariable Long templateId,
            Authentication auth
    ) {
        byte[] pdf = invoiceService.previewInvoice(id, templateId, auth.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice.pdf")
                .body(pdf);
    }

    // ================= SEND EMAIL =================
    @PostMapping("/{id}/template/{templateId}/send")
    public ResponseEntity<String> send(
            @PathVariable Long id,
            @PathVariable Long templateId,
            @RequestParam String email,
            Authentication auth
    ) {
        invoiceService.sendInvoice(id, templateId, email, auth.getName());
        return ResponseEntity.ok("Invoice sent successfully");
    }
}
