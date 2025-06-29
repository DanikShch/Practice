package practice.internetshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.cart.CartDto;
import practice.internetshop.exception.product.ProductNotFoundException;
import practice.internetshop.exception.cart.CartItemNotFoundException;
import practice.internetshop.exception.cart.InvalidQuantityException;
import practice.internetshop.mapper.CartMapper;
import practice.internetshop.model.*;
import practice.internetshop.repository.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Transactional(readOnly = true)
    public CartDto getCart(UserDetails userDetails) {
        User user = getUser(userDetails);
        loadUserCart(user);
        return cartMapper.toDto(user.getCart());
    }

    @Transactional
    public CartDto addItem(UserDetails userDetails, UUID productId, int quantity) {
        validateQuantity(quantity);

        User user = getUser(userDetails);
        Cart cart = loadUserCart(user);
        Product product = getProduct(productId);

        updateOrCreateCartItem(cart, product, quantity);
        return getRefreshedCart(cart);
    }

    @Transactional
    public CartDto updateItemQuantity(UserDetails userDetails, UUID productId, int quantity) {
        validateQuantity(quantity);

        User user = getUser(userDetails);
        Cart cart = loadUserCart(user);
        Product product = getProduct(productId);

        CartItem cartItem = getExistingCartItem(cart, product);
        cartItem.setQuantity(quantity);
        return getRefreshedCart(cart);
    }

    @Transactional
    public CartDto removeItem(UserDetails userDetails, UUID productId) {
        User user = getUser(userDetails);
        Cart cart = loadUserCart(user);
        cartItemRepository.deleteByCartAndProduct(cart.getId(), productId);
        return cartMapper.toDto(cart);
    }

    @Transactional
    public void clearCart(UserDetails userDetails) {
        User user = getUser(userDetails);
        Cart cart = loadUserCart(user);

        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Cart loadUserCart(User user) {
        if (user.getCart() == null) {
            Cart cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cartRepository.save(cart);
        }
        return cartRepository.findWithItemsByUser(user)
                .orElseThrow(() -> new IllegalStateException("Cart not loaded"));
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private void updateOrCreateCartItem(Cart cart, Product product, int quantity) {
        cartItemRepository.findByCartAndProduct(cart, product)
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(quantity);
                            cartItemRepository.save(newItem);
                        }
                );
    }

    private CartItem getExistingCartItem(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new CartItemNotFoundException(product.getId()));
    }

    private CartDto getRefreshedCart(Cart cart) {
        Cart refreshed = cartRepository.findWithItemsByUser(cart.getUser())
                .orElseThrow(() -> new IllegalStateException("Cart not found"));
        return cartMapper.toDto(refreshed);
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }
    }
}