package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.dto.TemplateSummaryResponse;
import in.NihalSingh.invoicegeneratorapi.entity.InvoiceTemplate;
import in.NihalSingh.invoicegeneratorapi.service.InvoiceTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final InvoiceTemplateService service;

    // ✅ GET ALL (ONLY id, name, active)
    @GetMapping
    public List<TemplateSummaryResponse> getAll() {
        return service.getAll();
    }
    /*

    @GetMapping("/{id}")
    public InvoiceTemplate getTemplate(@PathVariable Long id) {
        return service.getById(id);
    }

     */
}
