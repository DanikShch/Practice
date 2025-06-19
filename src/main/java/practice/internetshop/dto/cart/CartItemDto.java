package practice.internetshop.dto.cart;

import lombok.Data;
import practice.internetshop.dto.product.ProductDto;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID id;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal subTotal;

    public BigDecimal getSubTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}