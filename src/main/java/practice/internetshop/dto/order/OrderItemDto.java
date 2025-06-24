package practice.internetshop.dto.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDto {
    private UUID id;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}