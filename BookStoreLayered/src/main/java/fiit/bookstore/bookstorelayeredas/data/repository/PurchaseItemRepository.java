package fiit.bookstore.bookstorelayeredas.data.repository;

import fiit.bookstore.bookstorelayeredas.data.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for PurchaseItem entity.
 * Demonstrates data access abstraction for order items.
 */
@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, UUID> {

    /**
     * Find all items for a specific purchase.
     */
    List<PurchaseItem> findByPurchaseId(UUID purchaseId);

    /**
     * Find all purchase items for a specific book.
     */
    List<PurchaseItem> findByBookId(UUID bookId);

    /**
     * Count total quantity sold for a specific book.
     */
    @Query("SELECT COALESCE(SUM(pi.quantity), 0) FROM PurchaseItem pi WHERE pi.book.id = :bookId")
    Integer getTotalQuantitySoldForBook(@Param("bookId") UUID bookId);

    /**
     * Find top selling books (by quantity).
     */
    @Query("SELECT pi.book.id, SUM(pi.quantity) as totalSold FROM PurchaseItem pi " +
           "GROUP BY pi.book.id ORDER BY totalSold DESC")
    List<Object[]> findTopSellingBooks();
}
