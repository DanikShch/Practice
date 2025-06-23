package practice.internetshop.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import practice.internetshop.dto.order.OrderRequest;
import practice.internetshop.exception.cart.InsufficientStockException;
import practice.internetshop.model.*;
import practice.internetshop.repository.CartRepository;
import practice.internetshop.repository.OrderRepository;
import practice.internetshop.repository.ProductRepository;
import practice.internetshop.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(UserDetails userDetails, OrderRequest request) {
        // Получаем username из UserDetails
        String email = userDetails.getUsername();

        // Находим пользователя по username (или email, в зависимости от вашей реализации)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        // Остальная логика остается без изменений
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        validateCart(cart);

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setDeliveryAddress(request.getDeliveryAddress());

        List<OrderItem> orderItems = convertCartItemsToOrderItems(cart, order);
        order.setItems(orderItems);
        order.setTotalAmount(calculateTotal(orderItems));

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        emailService.sendOrderConfirmation(user.getEmail(), savedOrder);

        return savedOrder;
    }

    private void validateCart(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from empty cart");
        }

        cart.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName()
                );
            }
        });
    }


    private List<OrderItem> convertCartItemsToOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());

            // Обновление остатка товара
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return orderItem;
        }).toList();
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}