package in.NihalSingh.invoicegeneratorapi.repository;

import in.NihalSingh.invoicegeneratorapi.entity.InvoiceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceTemplateRepository extends JpaRepository<InvoiceTemplate, Long> {

    List<InvoiceTemplate> findByActiveTrue();

    // ✅ REQUIRED FOR TEMPLATE NAME BASED API
    Optional<InvoiceTemplate> findByNameIgnoreCaseAndActiveTrue(String name);
}
