package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import in.NihalSingh.invoicegeneratorapi.entity.InvoiceDetails;
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

    // ✅ CREATE INVOICE
    public Invoice saveInvoice(Invoice invoice, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        invoice.setUser(user);
        invoice.setCreatedAt(Instant.now());

        if (invoice.getInvoice() == null) {
            invoice.setInvoice(new InvoiceDetails());
        }

        invoice.getInvoice()
                .setNumber("INV-" + UUID.randomUUID().toString().substring(0, 8));

        return invoiceRepository.save(invoice);
    }

    // ✅ GET ALL INVOICES
    public List<Invoice> fetchInvoices(String username) {
        return invoiceRepository.findByUserUsername(username);
    }

    // ✅ GET SINGLE INVOICE
    public Invoice getInvoiceById(String username, Long id) {
        return invoiceRepository
                .findByUserUsernameAndId(username, id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Invoice not found"
                        )
                );
    }

    // ✅ DELETE
    public void removeInvoice(String username, Long id) {
        Invoice invoice = getInvoiceById(username, id);
        invoiceRepository.delete(invoice);
    }
}
