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

    // ✅ CREATE TEMPLATE
    @PostMapping
    public InvoiceTemplate create(@RequestBody InvoiceTemplate template) {
        return service.create(template);
    }

    // ✅ UPDATE TEMPLATE
    @PutMapping("/{id}")
    public InvoiceTemplate update(
            @PathVariable Long id,
            @RequestBody InvoiceTemplate template
    ) {
        return service.update(id, template);
    }

    // ✅ SOFT DELETE TEMPLATE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ✅ GET ALL TEMPLATES (ADMIN)
    @GetMapping
    public List<InvoiceTemplate> getAll() {
        return service.getAll();
    }
}
