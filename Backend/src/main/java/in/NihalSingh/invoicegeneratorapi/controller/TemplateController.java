package in.NihalSingh.invoicegeneratorapi.controller;

import in.NihalSingh.invoicegeneratorapi.entity.InvoiceTemplate;
import in.NihalSingh.invoicegeneratorapi.service.InvoiceTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final InvoiceTemplateService service;

    // ✅ User sees only active templates
    @GetMapping
    public ResponseEntity<List<InvoiceTemplate>> getActive() {
        return ResponseEntity.ok(service.getActiveTemplates());
    }

    // ✅ User selects template
    @GetMapping("/{id}")
    public InvoiceTemplate getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ User uploads company logo (NOT template logo)
    @PostMapping("/{id}/logo")
    public ResponseEntity<String> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        InvoiceTemplate template = service.getById(id);

        String base64 = Base64.getEncoder()
                .encodeToString(file.getBytes());

        template.setLogoBase64(base64);
        service.save(template);

        return ResponseEntity.ok("Logo uploaded successfully");
    }
}
