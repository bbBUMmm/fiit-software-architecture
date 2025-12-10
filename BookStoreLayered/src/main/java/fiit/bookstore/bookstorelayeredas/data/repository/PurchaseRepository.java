package fiit.bookstore.bookstorelayeredas.data.repository;

import fiit.bookstore.bookstorelayeredas.data.entity.Purchase;
import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Purchase entity.
 * Demonstrates:
 * - Data access abstraction
 * - Spring Data JPA automatic query generation
 * - Custom query methods using @Query
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    /**
     * Find purchase by order number.
     */
    Optional<Purchase> findByOrderNumber(String orderNumber);

    /**
     * Find all purchases by customer email.
     */
    List<Purchase> findByCustomerEmailIgnoreCase(String customerEmail);

    /**
     * Find all purchases by customer name (partial match).
     */
    List<Purchase> findByCustomerNameContainingIgnoreCase(String customerName);

    /**
     * Find all purchases by status.
     */
    List<Purchase> findByStatus(PurchaseStatus status);

    /**
     * Find all purchases within a date range.
     */
    @Query("SELECT p FROM Purchase p WHERE p.purchaseDate BETWEEN :startDate AND :endDate ORDER BY p.purchaseDate DESC")
    List<Purchase> findByPurchaseDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find recent purchases (last N days).
     */
    @Query("SELECT p FROM Purchase p WHERE p.purchaseDate >= :since ORDER BY p.purchaseDate DESC")
    List<Purchase> findRecentPurchases(@Param("since") LocalDateTime since);

    /**
     * Find all purchases by customer email and status.
     */
    List<Purchase> findByCustomerEmailIgnoreCaseAndStatus(String customerEmail, PurchaseStatus status);

    /**
     * Count purchases by status.
     */
    long countByStatus(PurchaseStatus status);

    /**
     * Check if order number exists.
     */
    boolean existsByOrderNumber(String orderNumber);
}
