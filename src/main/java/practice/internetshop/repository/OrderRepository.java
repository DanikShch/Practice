package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
