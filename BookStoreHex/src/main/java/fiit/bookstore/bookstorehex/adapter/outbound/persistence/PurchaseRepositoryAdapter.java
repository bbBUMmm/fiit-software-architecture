package fiit.bookstore.bookstorehex.adapter.outbound.persistence;

import fiit.bookstore.bookstorehex.domain.entity.Purchase;
import fiit.bookstore.bookstorehex.domain.entity.PurchaseStatus;
import fiit.bookstore.bookstorehex.domain.port.outbound.PurchaseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementation for PurchaseRepository outbound port.
 * This adapter translates between the domain port and the JPA repository.
 */
@Component
public class PurchaseRepositoryAdapter implements PurchaseRepository {

    private final JpaPurchaseRepository jpaPurchaseRepository;

    public PurchaseRepositoryAdapter(JpaPurchaseRepository jpaPurchaseRepository) {
        this.jpaPurchaseRepository = jpaPurchaseRepository;
    }

    @Override
    public Purchase save(Purchase purchase) {
        return jpaPurchaseRepository.save(purchase);
    }

    @Override
    public Optional<Purchase> findById(UUID id) {
        return jpaPurchaseRepository.findById(id);
    }

    @Override
    public Optional<Purchase> findByOrderNumber(String orderNumber) {
        return jpaPurchaseRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public List<Purchase> findByCustomerEmailIgnoreCase(String customerEmail) {
        return jpaPurchaseRepository.findByCustomerEmailIgnoreCase(customerEmail);
    }

    @Override
    public List<Purchase> findByStatus(PurchaseStatus status) {
        return jpaPurchaseRepository.findByStatus(status);
    }

    @Override
    public List<Purchase> findAll() {
        return jpaPurchaseRepository.findAll();
    }

    @Override
    public long count() {
        return jpaPurchaseRepository.count();
    }

    @Override
    public long countByStatus(PurchaseStatus status) {
        return jpaPurchaseRepository.countByStatus(status);
    }
}

