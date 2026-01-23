package in.NihalSingh.invoicegeneratorapi.repository;

import in.NihalSingh.invoicegeneratorapi.entity.InvoiceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceTemplateRepository
        extends JpaRepository<InvoiceTemplate, Long> {

    // ✅ CORRECT METHOD (NO ARGUMENT)
    List<InvoiceTemplate> findByActiveTrue();
}
