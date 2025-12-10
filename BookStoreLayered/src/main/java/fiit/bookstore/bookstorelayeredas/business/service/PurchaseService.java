package fiit.bookstore.bookstorelayeredas.business.service;

import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseRequest;
import fiit.bookstore.bookstorelayeredas.business.dto.PurchaseResponse;
import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseStatus;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Purchase operations.
 * Demonstrates:
 * - Abstraction: Interface defines contract without implementation details
 * - Rich domain logic beyond simple CRUD
 * - Business operations for buying books
 */
public interface PurchaseService {

    /**
     * Create a new purchase (buy books).
     * This is the main business operation that:
     * - Validates stock availability
     * - Applies discounts if applicable
     * - Reduces book inventory
     * - Creates the purchase record
     *
     * @param request the purchase request containing items and customer info
     * @return the created purchase response
     */
    PurchaseResponse createPurchase(PurchaseRequest request);

    /**
     * Get a purchase by its ID.
     * @param id the purchase ID
     * @return the purchase response
     */
    PurchaseResponse getPurchaseById(UUID id);

    /**
     * Get a purchase by its order number.
     * @param orderNumber the order number
     * @return the purchase response
     */
    PurchaseResponse getPurchaseByOrderNumber(String orderNumber);

    /**
     * Get all purchases for a customer by email.
     * @param email the customer email
     * @return list of purchases
     */
    List<PurchaseResponse> getPurchasesByCustomerEmail(String email);

    /**
     * Get all purchases with a specific status.
     * @param status the purchase status
     * @return list of purchases
     */
    List<PurchaseResponse> getPurchasesByStatus(PurchaseStatus status);

    /**
     * Get all purchases.
     * @return list of all purchases
     */
    List<PurchaseResponse> getAllPurchases();

    /**
     * Confirm a pending purchase.
     * @param id the purchase ID
     * @return the updated purchase response
     */
    PurchaseResponse confirmPurchase(UUID id);

    /**
     * Cancel a purchase.
     * This also restores the book inventory.
     * @param id the purchase ID
     * @return the updated purchase response
     */
    PurchaseResponse cancelPurchase(UUID id);

    /**
     * Update purchase status.
     * @param id the purchase ID
     * @param status the new status
     * @return the updated purchase response
     */
    PurchaseResponse updatePurchaseStatus(UUID id, PurchaseStatus status);

    /**
     * Apply a discount code to a pending purchase.
     * @param id the purchase ID
     * @param discountCode the discount code
     * @return the updated purchase response
     */
    PurchaseResponse applyDiscountCode(UUID id, String discountCode);

    /**
     * Get purchase statistics summary.
     * @return purchase statistics
     */
    PurchaseStatistics getPurchaseStatistics();

    /**
     * Statistics class for purchase analytics.
     */
    record PurchaseStatistics(
            long totalPurchases,
            long pendingPurchases,
            long confirmedPurchases,
            long cancelledPurchases,
            java.math.BigDecimal totalRevenue
    ) {}
}
