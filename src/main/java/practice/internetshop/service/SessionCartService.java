package practice.internetshop.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.cart.CartDto;
import practice.internetshop.dto.cart.CartItemDto;
import practice.internetshop.exception.product.ProductNotFoundException;
import practice.internetshop.exception.cart.CartItemNotFoundException;
import practice.internetshop.exception.cart.InvalidQuantityException;
import practice.internetshop.mapper.ProductMapper;
import practice.internetshop.model.Product;
import practice.internetshop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionCartService {
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ProductMapper productMapper;

    public CartDto getCart(HttpSession session) {
        return getOrCreateCart(session);
    }

    @Transactional
    public CartDto addItem(HttpSession session, UUID productId, int quantity) {
        validateQuantity(quantity);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        CartDto cart = getOrCreateCart(session);
        addOrUpdateItem(cart, product, quantity);
        return cart;
    }

    public CartDto updateItemQuantity(HttpSession session, UUID productId, int quantity) {
        validateQuantity(quantity);

        CartDto cart = getOrCreateCart(session);
        updateItem(cart, productId, quantity);
        return cart;
    }

    public CartDto removeItem(HttpSession session, UUID productId) {
        CartDto cart = getOrCreateCart(session);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cart;
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public void mergeToUserCart(HttpSession session, UserDetails userDetails) {
        CartDto guestCart = (CartDto) session.getAttribute("cart");
        if (guestCart != null && !guestCart.getItems().isEmpty()) {
            for (CartItemDto item : guestCart.getItems()) {
                cartService.addItem(userDetails, item.getProduct().getId(), item.getQuantity());
            }
            clearCart(session);
        }
    }

    private CartDto getOrCreateCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartDto();
            cart.setItems(new ArrayList<>());
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void addOrUpdateItem(CartDto cart, Product product, int quantity) {
        Optional<CartItemDto> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItemDto newItem = new CartItemDto();
            newItem.setProduct(productMapper.toDto(product));
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
    }

    private void updateItem(CartDto cart, UUID productId, int quantity) {
        CartItemDto item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(productId));
        item.setQuantity(quantity);
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }
    }
}