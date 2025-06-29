package practice.internetshop.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import practice.internetshop.dto.order.OrderRequest;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.exception.cart.InsufficientStockException;
import practice.internetshop.exception.promo.PromoCodeInvalidException;
import practice.internetshop.exception.promo.PromoCodeNotFoundException;
import practice.internetshop.exception.user.AccessException;
import practice.internetshop.exception.user.EmailException;
import practice.internetshop.exception.user.EmailSendingException;
import practice.internetshop.mapper.OrderMapper;
import practice.internetshop.mapper.PromoCodeMapper;
import practice.internetshop.model.*;
import practice.internetshop.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    @Transactional
    public OrderResponseDto createOrder(UserDetails userDetails, OrderRequest request) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        validateCart(cart);
        Order order = orderMapper.toEntity(request, user);
        List<OrderItem> orderItems = orderMapper.toOrderItems(cart.getItems(), order);

        updateProductStocks(orderItems);

        order.setItems(orderItems);
        BigDecimal originalAmount = orderMapper.calculateTotal(orderItems);
        order.setOriginalAmount(originalAmount);

        if (request.getPromoCode() != null && !request.getPromoCode().isEmpty()) {
            try {
                PromoCode promoCode = promoCodeRepository.findByCode(request.getPromoCode())
                        .orElseThrow(() -> new PromoCodeNotFoundException(request.getPromoCode()));

                if (!promoCode.isValid()) {
                    throw new PromoCodeInvalidException(request.getPromoCode());
                }

                BigDecimal discount = promoCodeMapper.calculateDiscount(promoCode, originalAmount);
                BigDecimal totalAmount = originalAmount.subtract(discount);

                order.setTotalAmount(totalAmount);
                order.setAppliedPromoCode(request.getPromoCode());
                promoCode.incrementUsage();
                promoCodeRepository.save(promoCode);
            } catch (PromoCodeNotFoundException | PromoCodeInvalidException e) {
                order.setTotalAmount(originalAmount);
            }
        } else {
            order.setTotalAmount(originalAmount);
        }

        Order savedOrder = orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);

        emailService.sendOrderConfirmation(user.getEmail(), savedOrder);
        return orderMapper.toDto(savedOrder);
    }

    private void updateProductStocks(List<OrderItem> orderItems) {
        orderItems.forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        });
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

    public List<OrderResponseDto> getUserOrders(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDto getUserOrder(UserDetails userDetails, UUID orderId) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new EmailSendingException("You don't have permission to access this order");
        }

        return orderMapper.toDto(order);
    }

    @Transactional
    public void cancelOrder(UserDetails userDetails, UUID orderId) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessException("You can only cancel your own orders");
        }

        if (!order.getStatus().canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in its current status");
        }
        returnProductsToStock(order);

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        emailService.sendOrderCancellation(user.getEmail(), order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getOrdersByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return orderRepository.findByUser(user).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void adminCancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getStatus().canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in its current status");
        }

        returnProductsToStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        emailService.sendOrderCancellation(order.getUser().getEmail(), order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid status transition");
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        if (newStatus.shouldNotifyUser()) {
            emailService.sendOrderStatusUpdate(order.getUser().getEmail(), order);
        }

        return orderMapper.toDto(updatedOrder);
    }

    private void returnProductsToStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }
}