package practice.internetshop.dto.order;

import lombok.Data;
import practice.internetshop.model.Order.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID id;
    private LocalDateTime orderDate;
    private BigDecimal originalAmount;
    private BigDecimal totalAmount;
    private String appliedPromoCode;
    private OrderStatus status;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private DeliveryMethod deliveryMethod;
    private List<OrderItemDto> items;
    private BigDecimal discountAmount;

    private UUID userId;
    private String userEmail;
}