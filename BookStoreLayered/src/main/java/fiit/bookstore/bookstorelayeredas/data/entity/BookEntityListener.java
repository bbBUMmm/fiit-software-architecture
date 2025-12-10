package fiit.bookstore.bookstorelayeredas.data.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Entity listener for Book entity to handle lifecycle callbacks.
 */
public class BookEntityListener {

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
