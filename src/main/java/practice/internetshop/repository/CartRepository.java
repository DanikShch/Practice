package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.Cart;
import practice.internetshop.model.User;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(User user);
}
