package fiit.bookstore.bookstorehex.adapter.outbound.persistence;

import fiit.bookstore.bookstorehex.domain.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

/**
 * Entity listener to handle lifecycle callbacks for entities.
 */
public class EntityListener {

    @PrePersist
    public void onCreate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            LocalDateTime now = LocalDateTime.now();
            baseEntity.setCreatedAt(now);
            baseEntity.setUpdatedAt(now);
        }
    }

    @PreUpdate
    public void onUpdate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setUpdatedAt(LocalDateTime.now());
        }
    }
}
