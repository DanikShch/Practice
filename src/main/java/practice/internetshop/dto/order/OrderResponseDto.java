package practice.internetshop.dto.order;

import lombok.Data;
import practice.internetshop.model.DeliveryMethod;
import practice.internetshop.model.OrderStatus;
import practice.internetshop.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private DeliveryMethod deliveryMethod;
    private List<OrderItemDto> items;

    private UUID userId;
    private String userEmail;
}