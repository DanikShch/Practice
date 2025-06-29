package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.WishlistItem;

import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<WishlistItem, UUID> {
    List<WishlistItem> findByUserId(UUID userId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
    void deleteByUserIdAndProductId(UUID userId, UUID productId);
    int countByUserId(UUID userId);
}