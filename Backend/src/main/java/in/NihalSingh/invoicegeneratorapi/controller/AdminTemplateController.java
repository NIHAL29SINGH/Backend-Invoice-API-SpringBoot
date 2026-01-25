package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.entity.InvoiceTemplate;
import in.NihalSingh.invoicegeneratorapi.service.InvoiceTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/templates")
@RequiredArgsConstructor
public class AdminTemplateController {

    private final InvoiceTemplateService service;

    // ✅ Create template
    @PostMapping
    public InvoiceTemplate create(@RequestBody InvoiceTemplate template) {
        return service.create(template);
    }

    // ✅ Update template
    @PutMapping("/{id}")
    public InvoiceTemplate update(
            @PathVariable Long id,
            @RequestBody InvoiceTemplate template
    ) {
        return service.update(id, template);
    }

    // ✅ Soft delete (disable)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        InvoiceTemplate template = service.getById(id); // ✅ FIX
        template.setActive(false);
        service.save(template);
    }

    // ✅ Admin view all
    @GetMapping
    public List<InvoiceTemplate> getAll() {
        return service.getAll();
    }
}
