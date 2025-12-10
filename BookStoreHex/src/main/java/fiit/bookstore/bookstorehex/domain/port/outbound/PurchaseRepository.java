package fiit.bookstore.bookstorehex.domain.port.outbound;

import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port (repository interface) for Purchase persistence.
 * In hexagonal architecture, outbound ports define the operations that the domain
 * needs from the outside world.
 */
public interface PurchaseRepository {

    /**
     * Save a purchase (create or update)
     * @param purchase the purchase to save
     * @return the saved purchase
     */
    Purchase save(Purchase purchase);

    /**
     * Find a purchase by its ID
     * @param id the purchase ID
     * @return Optional containing the purchase if found
     */
    Optional<Purchase> findById(UUID id);

    /**
     * Find purchase by order number.
     * @param orderNumber the order number
     * @return Optional containing the purchase if found
     */
    Optional<Purchase> findByOrderNumber(String orderNumber);

    /**
     * Find all purchases by customer email (case-insensitive).
     * @param customerEmail the customer email
     * @return list of purchases
     */
    List<Purchase> findByCustomerEmailIgnoreCase(String customerEmail);

    /**
     * Find all purchases by status.
     * @param status the purchase status
     * @return list of purchases
     */
    List<Purchase> findByStatus(PurchaseStatus status);

    /**
     * Find all purchases.
     * @return list of all purchases
     */
    List<Purchase> findAll();

    /**
     * Count all purchases.
     * @return the count
     */
    long count();

    /**
     * Count purchases by status.
     * @param status the purchase status
     * @return the count
     */
    long countByStatus(PurchaseStatus status);
}

