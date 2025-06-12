package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.OrderItem;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
