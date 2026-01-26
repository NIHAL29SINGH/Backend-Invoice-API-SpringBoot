package in.NihalSingh.invoicegeneratorapi.repository;

import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // ✅ USED FOR USER INVOICE LIST
    List<Invoice> findByUserEmail(String email);

    // ✅ REQUIRED FOR PREVIEW / SEND / DELETE
    Optional<Invoice> findByUserEmailAndId(String email, Long id);

    // ✅ ADMIN ANALYTICS
    Long countByUserId(Long userId);

    @Query("""
        SELECT DISTINCT t.name
        FROM Invoice i
        JOIN i.template t
        WHERE i.user.id = :userId
    """)
    List<String> findUsedTemplateNames(@Param("userId") Long userId);
}
