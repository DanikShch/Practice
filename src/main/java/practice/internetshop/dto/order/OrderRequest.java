package practice.internetshop.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import practice.internetshop.model.DeliveryMethod;
import practice.internetshop.model.PaymentMethod;

@Data
public class OrderRequest {
    @NotNull
    public DeliveryMethod deliveryMethod;

    @NotNull
    public PaymentMethod paymentMethod;

    @NotBlank
    public String deliveryAddress;
}
