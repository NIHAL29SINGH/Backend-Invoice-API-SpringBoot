package in.NihalSingh.invoicegeneratorapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import in.NihalSingh.invoicegeneratorapi.dto.InvoiceSummaryResponse;
import in.NihalSingh.invoicegeneratorapi.entity.*;
import in.NihalSingh.invoicegeneratorapi.repository.InvoiceRepository;
import in.NihalSingh.invoicegeneratorapi.repository.InvoiceTemplateRepository;
import in.NihalSingh.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceTemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final PdfService pdfService;
    private final EmailService emailService;

    // ================= CREATE =================
    public Invoice createInvoice(String invoiceJson, MultipartFile logo, String email) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            Invoice invoice = mapper.readValue(invoiceJson, Invoice.class);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (invoice.getTemplate() == null || invoice.getTemplate().getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template is required");
            }

            InvoiceTemplate template = templateRepository
                    .findById(invoice.getTemplate().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found"));

            invoice.setUser(user);
            invoice.setTemplate(template);
            invoice.setStatus(InvoiceStatus.DRAFT);
            invoice.setCreatedAt(Instant.now());

            invoice.getInvoice()
                    .setNumber("INV-" + UUID.randomUUID().toString().substring(0, 8));

            if (logo != null && !logo.isEmpty()) {
                invoice.getCompany().setLogoBase64(
                        Base64.getEncoder().encodeToString(logo.getBytes())
                );
            }

            return invoiceRepository.save(invoice);

        } catch (Exception e) {
            throw new RuntimeException("Invalid invoice data", e);
        }
    }

    // ================= LIST =================
    public List<InvoiceSummaryResponse> getAllInvoices(String email) {
        return invoiceRepository.findByUserEmail(email)
                .stream()
                .map(i -> new InvoiceSummaryResponse(
                        i.getId(),
                        i.getInvoice().getNumber(),
                        i.getCompany().getName(),
                        i.getStatus().name(),
                        i.getCreatedAt()
                ))
                .toList();
    }

    // ================= DELETE =================
    public void deleteInvoice(Long id, String email) {
        Invoice invoice = getInvoice(id, email);
        invoiceRepository.delete(invoice);
    }

    // ================= PREVIEW =================
    public byte[] previewInvoice(Long id, Long templateId, String email) {
        Invoice invoice = getInvoice(id, email);

        InvoiceTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found"));

        invoice.setTemplate(template);
        return pdfService.generateInvoicePdf(invoice);
    }

    // ================= SEND =================
    public void sendInvoice(Long id, Long templateId, String toEmail, String userEmail) {
        Invoice invoice = getInvoice(id, userEmail);

        InvoiceTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found"));

        invoice.setTemplate(template);

        emailService.sendInvoiceEmail(toEmail, invoice);
        invoice.setStatus(InvoiceStatus.SENT);
        invoiceRepository.save(invoice);
    }

    private Invoice getInvoice(Long id, String email) {
        return invoiceRepository.findByUserEmailAndId(email, id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
    }
}
