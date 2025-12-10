package fiit.bookstore.bookstorehex.domain.port.inbound;

import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port (use case) for Purchase operations.
 * In hexagonal architecture, inbound ports define the operations that the application
 * exposes to the outside world.
 */
public interface PurchaseUseCase {

    /**
     * Create a new purchase (buy books).
     * This is the main business operation that:
     * - Validates stock availability
     * - Applies discounts if applicable
     * - Reduces book inventory
     * - Creates the purchase record
     *
     * @param purchase the purchase to create
     * @param discountCode optional discount code
     * @return the created purchase
     */
    Purchase createPurchase(Purchase purchase, String discountCode);

    /**
     * Get a purchase by its ID.
     * @param id the purchase ID
     * @return the purchase
     */
    Purchase getPurchaseById(UUID id);

    /**
     * Get a purchase by its order number.
     * @param orderNumber the order number
     * @return the purchase
     */
    Purchase getPurchaseByOrderNumber(String orderNumber);

    /**
     * Get all purchases for a customer by email.
     * @param email the customer email
     * @return list of purchases
     */
    List<Purchase> getPurchasesByCustomerEmail(String email);

    /**
     * Get all purchases with a specific status.
     * @param status the purchase status
     * @return list of purchases
     */
    List<Purchase> getPurchasesByStatus(PurchaseStatus status);

    /**
     * Get all purchases.
     * @return list of all purchases
     */
    List<Purchase> getAllPurchases();

    /**
     * Confirm a pending purchase.
     * @param id the purchase ID
     * @return the updated purchase
     */
    Purchase confirmPurchase(UUID id);

    /**
     * Cancel a purchase.
     * This also restores the book inventory.
     * @param id the purchase ID
     * @return the updated purchase
     */
    Purchase cancelPurchase(UUID id);

    /**
     * Update purchase status.
     * @param id the purchase ID
     * @param status the new status
     * @return the updated purchase
     */
    Purchase updatePurchaseStatus(UUID id, PurchaseStatus status);

    /**
     * Apply a discount code to a pending purchase.
     * @param id the purchase ID
     * @param discountCode the discount code
     * @return the updated purchase
     */
    Purchase applyDiscountCode(UUID id, String discountCode);

    /**
     * Get purchase statistics summary.
     * @return purchase statistics
     */
    PurchaseStatistics getPurchaseStatistics();

    /**
     * Statistics record for purchase analytics.
     */
    record PurchaseStatistics(
            long totalPurchases,
            long pendingPurchases,
            long confirmedPurchases,
            long cancelledPurchases,
            BigDecimal totalRevenue
    ) {}
}

