package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.Order;
import practice.internetshop.model.User;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}
