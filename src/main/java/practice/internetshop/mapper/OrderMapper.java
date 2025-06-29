package practice.internetshop.mapper;

import practice.internetshop.dto.order.OrderItemDto;
import practice.internetshop.dto.order.OrderRequest;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.model.CartItem;
import practice.internetshop.model.Order;
import practice.internetshop.model.OrderItem;
import practice.internetshop.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface OrderMapper {
    OrderResponseDto toDto(Order order);
    OrderItemDto toItemDto(OrderItem item);
    Order toEntity(OrderRequest request, User user);
    OrderItem toOrderItem(CartItem cartItem, Order order);
    List<OrderItem> toOrderItems(List<CartItem> cartItems, Order order);
    BigDecimal calculateTotal(List<OrderItem> items);
    BigDecimal calculateDiscount(Order order);
}
