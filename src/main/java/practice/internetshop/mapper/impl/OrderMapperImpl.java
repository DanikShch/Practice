package practice.internetshop.mapper.impl;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.order.OrderItemDto;
import practice.internetshop.dto.order.OrderRequest;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.mapper.OrderMapper;
import practice.internetshop.model.*;
import practice.internetshop.model.Order.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponseDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOriginalAmount(order.getOriginalAmount());
        dto.setAppliedPromoCode(order.getAppliedPromoCode());
        dto.setDiscountAmount(calculateDiscount(order));
        dto.setStatus(order.getStatus());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setDeliveryMethod(order.getDeliveryMethod());

        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
            dto.setUserEmail(order.getUser().getEmail());
        }

        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null || order.getOriginalAmount() == null || order.getTotalAmount() == null) {
            return BigDecimal.ZERO;
        }
        return order.getOriginalAmount().subtract(order.getTotalAmount());
    }

    @Override
    public Order toEntity(OrderRequest request, User user) {
        if (request == null || user == null) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    @Override
    public OrderItem toOrderItem(CartItem cartItem, Order order) {
        if (cartItem == null || order == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUnitPrice(cartItem.getProduct().getPrice());
        return orderItem;
    }

    @Override
    public List<OrderItem> toOrderItems(List<CartItem> cartItems, Order order) {
        if (cartItems == null || order == null) {
            return List.of();
        }

        return cartItems.stream()
                .map(cartItem -> toOrderItem(cartItem, order))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateTotal(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderItemDto toItemDto(OrderItem item) {
        if (item == null) {
            return null;
        }

        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }
}