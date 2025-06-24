package practice.internetshop.mapper;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.order.OrderItemDto;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.model.Order;
import practice.internetshop.model.OrderItem;

import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
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

    public OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }
}