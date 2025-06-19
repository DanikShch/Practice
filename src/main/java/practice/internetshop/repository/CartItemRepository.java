package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.internetshop.model.Cart;
import practice.internetshop.model.CartItem;
import practice.internetshop.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart = :cart AND ci.product = :product")
    void deleteByCartAndProduct(@Param("cart") Cart cart, @Param("product") Product product);

    void deleteAllByCart(Cart cart);
}
