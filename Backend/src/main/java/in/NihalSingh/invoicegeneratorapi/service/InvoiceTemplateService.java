package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.entity.InvoiceTemplate;
import in.NihalSingh.invoicegeneratorapi.repository.InvoiceTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceTemplateService {

    private final InvoiceTemplateRepository repository;

    // =============================
    // GET ALL
    // =============================
    public List<InvoiceTemplate> getAll() {
        return repository.findAll();
    }

    // =============================
    // GET ACTIVE
    // =============================
    public List<InvoiceTemplate> getActiveTemplates() {
        return repository.findByActiveTrue();
    }

    // =============================
    // GET BY ID
    // =============================
    public InvoiceTemplate getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    // =============================
    // CREATE
    // =============================
    public InvoiceTemplate create(InvoiceTemplate template) {
        return repository.save(template);
    }

    // =============================
    // ✅ REQUIRED FIX
    // =============================
    public InvoiceTemplate save(InvoiceTemplate template) {
        return repository.save(template);
    }

    // =============================
    // UPDATE
    // =============================
    public InvoiceTemplate update(Long id, InvoiceTemplate template) {
        InvoiceTemplate existing = getById(id);
        existing.setName(template.getName());
        existing.setHtmlTemplate(template.getHtmlTemplate());
        existing.setActive(template.isActive());
        return repository.save(existing);
    }

    // =============================
    // SOFT DELETE
    // =============================
    public void delete(Long id) {
        InvoiceTemplate template = getById(id);
        template.setActive(false);
        repository.save(template);
    }
}
