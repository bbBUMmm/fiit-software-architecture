package fiit.bookstore.bookstorehex.adapter.outbound.persistence;

import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Purchase persistence.
 * This is internal to the outbound adapter.
 */
@Repository
interface JpaPurchaseRepository extends JpaRepository<Purchase, UUID> {

    Optional<Purchase> findByOrderNumber(String orderNumber);

    List<Purchase> findByCustomerEmailIgnoreCase(String customerEmail);

    List<Purchase> findByStatus(PurchaseStatus status);

    long countByStatus(PurchaseStatus status);
}

