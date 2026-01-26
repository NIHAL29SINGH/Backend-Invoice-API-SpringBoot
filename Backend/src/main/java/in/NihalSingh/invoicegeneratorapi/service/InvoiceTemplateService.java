package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.dto.TemplateSummaryResponse;
import in.NihalSingh.invoicegeneratorapi.dto.TemplateSummaryResponse;
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
    // GET ALL (ADMIN)
    // =============================
    public List<TemplateSummaryResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(t -> new TemplateSummaryResponse(
                        t.getId(),
                        t.getName(),
                        t.isActive()
                ))
                .toList();
    }

    // =============================
    // GET ACTIVE (USER SIDE)
    // =============================
    public List<TemplateSummaryResponse> getActiveTemplates() {
        return repository.findByActiveTrue()
                .stream()
                .map(t -> new TemplateSummaryResponse(
                        t.getId(),
                        t.getName(),
                        t.isActive()
                ))
                .toList();
    }

    // =============================
    // GET BY ID (FULL)
    // =============================
    public InvoiceTemplate getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    // =============================
    // CREATE
    // =============================
    public InvoiceTemplate create(InvoiceTemplate template) {
        template.setActive(true);
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
    // HARD DELETE
    // =============================
    public void hardDelete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Template not found");
        }
        repository.deleteById(id);
    }
}
