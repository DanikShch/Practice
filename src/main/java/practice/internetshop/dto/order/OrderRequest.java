package practice.internetshop.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import practice.internetshop.model.Order.*;

@Data
public class OrderRequest {
    @NotNull
    public DeliveryMethod deliveryMethod;

    @NotNull
    public PaymentMethod paymentMethod;

    @NotBlank
    public String deliveryAddress;

    private String promoCode;
}
