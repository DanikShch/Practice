package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

}
