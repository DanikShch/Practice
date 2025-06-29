package practice.internetshop.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import practice.internetshop.model.Order.*;

@Data
public class OrderStatusUpdateRequest {
    @NotNull
    private OrderStatus status;
}
