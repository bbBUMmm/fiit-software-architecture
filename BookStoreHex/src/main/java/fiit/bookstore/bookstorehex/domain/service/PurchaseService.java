package fiit.bookstore.bookstorehex.domain.service;

import fiit.bookstore.bookstorehex.domain.entity.Book;
import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseItem;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;
import fiit.bookstore.bookstorehex.domain.exception.*;
import fiit.bookstore.bookstorehex.domain.port.inbound.PurchaseUseCase;
import fiit.bookstore.bookstorehex.domain.port.outbound.BookRepository;
import fiit.bookstore.bookstorehex.domain.port.outbound.PurchaseRepository;

import java.math.BigDecimal;
import java.util.*;

public class PurchaseService implements PurchaseUseCase {

    private final PurchaseRepository purchaseRepository;
    private final BookRepository bookRepository;

    private static final Map<String, DiscountConfig> DISCOUNT_CODES = Map.of(
            "SAVE10", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(10)),
            "SAVE20", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(20)),
            "FLAT5", new DiscountConfig(DiscountType.FIXED, BigDecimal.valueOf(5)),
            "FLAT10", new DiscountConfig(DiscountType.FIXED, BigDecimal.valueOf(10)),
            "WELCOME", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(15)),
            "BOOKWORM", new DiscountConfig(DiscountType.PERCENTAGE, BigDecimal.valueOf(25))
    );

    public PurchaseService(PurchaseRepository purchaseRepository, BookRepository bookRepository) {
        this.purchaseRepository = purchaseRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Purchase createPurchase(Purchase purchase, String discountCode) {
        Map<UUID, Book> bookMap = validateAndCollectBooks(purchase.getItems());
        validateStockAvailability(purchase.getItems(), bookMap);

        for (PurchaseItem item : purchase.getItems()) {
            Book book = bookMap.get(item.getBook().getId());
            book.reduceQuantity(item.getQuantity());
            bookRepository.save(book);
        }

        if (discountCode != null && !discountCode.isBlank()) {
            applyDiscountInternal(purchase, discountCode);
        }

        purchase.recalculateTotals();
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase getPurchaseById(UUID id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
    }

    @Override
    public Purchase getPurchaseByOrderNumber(String orderNumber) {
        return purchaseRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new PurchaseNotFoundException(orderNumber));
    }

    @Override
    public List<Purchase> getPurchasesByCustomerEmail(String email) {
        return purchaseRepository.findByCustomerEmailIgnoreCase(email);
    }

    @Override
    public List<Purchase> getPurchasesByStatus(PurchaseStatus status) {
        return purchaseRepository.findByStatus(status);
    }

    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public Purchase confirmPurchase(UUID id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        if (purchase.getStatus() != PurchaseStatus.PENDING) {
            throw new InvalidPurchaseStateException(
                    String.format("Cannot confirm purchase in status: %s", purchase.getStatus()));
        }

        purchase.confirm();
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase cancelPurchase(UUID id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        if (purchase.getStatus() == PurchaseStatus.SHIPPED ||
            purchase.getStatus() == PurchaseStatus.DELIVERED ||
            purchase.getStatus() == PurchaseStatus.CANCELLED) {
            throw new InvalidPurchaseStateException(
                    String.format("Cannot cancel purchase in status: %s", purchase.getStatus()));
        }

        for (PurchaseItem item : purchase.getItems()) {
            Book book = item.getBook();
            if (book != null) {
                book.addQuantity(item.getQuantity());
                bookRepository.save(book);
            }
        }

        purchase.cancel();
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase updatePurchaseStatus(UUID id, PurchaseStatus status) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        validateStatusTransition(purchase.getStatus(), status);

        purchase.setStatus(status);
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase applyDiscountCode(UUID id, String discountCode) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));

        if (!purchase.isModifiable()) {
            throw new InvalidPurchaseStateException("Cannot apply discount to a non-pending purchase");
        }

        applyDiscountInternal(purchase, discountCode);
        return purchaseRepository.save(purchase);
    }

    @Override
    public PurchaseStatistics getPurchaseStatistics() {
        long total = purchaseRepository.count();
        long pending = purchaseRepository.countByStatus(PurchaseStatus.PENDING);
        long confirmed = purchaseRepository.countByStatus(PurchaseStatus.CONFIRMED);
        long cancelled = purchaseRepository.countByStatus(PurchaseStatus.CANCELLED);

        BigDecimal revenue = purchaseRepository.findAll().stream()
                .filter(p -> p.getStatus() != PurchaseStatus.CANCELLED &&
                            p.getStatus() != PurchaseStatus.REFUNDED)
                .map(Purchase::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PurchaseStatistics(total, pending, confirmed, cancelled, revenue);
    }

    private Map<UUID, Book> validateAndCollectBooks(List<PurchaseItem> items) {
        Map<UUID, Book> bookMap = new HashMap<>();
        for (PurchaseItem item : items) {
            UUID bookId = item.getBook().getId();
            if (!bookMap.containsKey(bookId)) {
                Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new BookNotFoundException(bookId));
                bookMap.put(bookId, book);
            }
        }
        return bookMap;
    }

    private void validateStockAvailability(List<PurchaseItem> items, Map<UUID, Book> bookMap) {
        Map<UUID, Integer> requestedQuantities = new HashMap<>();
        for (PurchaseItem item : items) {
            UUID bookId = item.getBook().getId();
            requestedQuantities.merge(bookId, item.getQuantity(), Integer::sum);
        }

        for (Map.Entry<UUID, Integer> entry : requestedQuantities.entrySet()) {
            Book book = bookMap.get(entry.getKey());
            int requested = entry.getValue();
            if (book.getQuantity() < requested) {
                throw new InsufficientStockException(book.getIsbn(), requested, book.getQuantity());
            }
        }
    }

    private void applyDiscountInternal(Purchase purchase, String discountCode) {
        String upperCode = discountCode.toUpperCase().trim();
        DiscountConfig config = DISCOUNT_CODES.get(upperCode);

        if (config == null) {
            throw new InvalidDiscountCodeException(discountCode);
        }

        if (config.type == DiscountType.PERCENTAGE) {
            purchase.applyPercentageDiscount(config.value, upperCode);
        } else {
            purchase.applyFixedDiscount(config.value, upperCode);
        }
    }

    private void validateStatusTransition(PurchaseStatus current, PurchaseStatus target) {
        Map<PurchaseStatus, Set<PurchaseStatus>> validTransitions = Map.of(
                PurchaseStatus.PENDING, Set.of(PurchaseStatus.CONFIRMED, PurchaseStatus.CANCELLED),
                PurchaseStatus.CONFIRMED, Set.of(PurchaseStatus.PROCESSING, PurchaseStatus.CANCELLED),
                PurchaseStatus.PROCESSING, Set.of(PurchaseStatus.SHIPPED, PurchaseStatus.CANCELLED),
                PurchaseStatus.SHIPPED, Set.of(PurchaseStatus.DELIVERED),
                PurchaseStatus.DELIVERED, Set.of(PurchaseStatus.REFUNDED),
                PurchaseStatus.CANCELLED, Set.of(),
                PurchaseStatus.REFUNDED, Set.of()
        );

        Set<PurchaseStatus> allowed = validTransitions.getOrDefault(current, Set.of());
        if (!allowed.contains(target)) {
            throw new InvalidPurchaseStateException(
                    String.format("Cannot transition from %s to %s", current, target));
        }
    }

    private enum DiscountType { PERCENTAGE, FIXED }
    private record DiscountConfig(DiscountType type, BigDecimal value) {}
}
