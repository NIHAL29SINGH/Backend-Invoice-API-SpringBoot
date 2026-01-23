package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import in.NihalSingh.invoicegeneratorapi.entity.InvoiceDetails;
import in.NihalSingh.invoicegeneratorapi.entity.InvoiceStatus;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import in.NihalSingh.invoicegeneratorapi.repository.InvoiceRepository;
import in.NihalSingh.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final PdfService pdfService;
    private final EmailService emailService;

    // =============================
    // CREATE INVOICE
    // =============================
    public Invoice saveInvoice(Invoice invoice, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        invoice.setUser(user);
        invoice.setCreatedAt(Instant.now());

        if (invoice.getInvoice() == null) {
            invoice.setInvoice(new InvoiceDetails());
        }

        invoice.getInvoice()
                .setNumber("INV-" + UUID.randomUUID().toString().substring(0, 8));

        return invoiceRepository.save(invoice);
    }

    // =============================
    // GET ALL
    // =============================
    public List<Invoice> fetchInvoices(String email) {
        return invoiceRepository.findByUserEmail(email);
    }

    // =============================
    // GET ONE
    // =============================
    public Invoice getInvoiceById(String email, Long id) {
        return invoiceRepository
                .findByUserEmailAndId(email, id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found")
                );
    }

    // =============================
    // DELETE
    // =============================
    public void removeInvoice(String email, Long id) {
        Invoice invoice = getInvoiceById(email, id);
        invoiceRepository.delete(invoice);
    }

    // =============================
    // PREVIEW PDF
    // =============================
    public byte[] previewInvoice(Long id, String email) {
        Invoice invoice = getInvoiceById(email, id);
        return pdfService.generateInvoicePdf(invoice);
    }

    // =============================
    // SEND INVOICE
    // =============================
    public void sendInvoice(Long id, String emailToSend, String email) {

        Invoice invoice = getInvoiceById(email, id);

        emailService.sendInvoiceEmail(emailToSend, invoice);

        invoice.setStatus(InvoiceStatus.SENT);
        invoiceRepository.save(invoice);
    }
}
