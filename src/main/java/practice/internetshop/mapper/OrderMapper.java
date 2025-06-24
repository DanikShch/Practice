package practice.internetshop.mapper;

import practice.internetshop.dto.order.OrderItemDto;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.model.Order;
import practice.internetshop.model.OrderItem;

public interface OrderMapper {
    OrderResponseDto toDto(Order order);
    OrderItemDto toItemDto(OrderItem item);
}
