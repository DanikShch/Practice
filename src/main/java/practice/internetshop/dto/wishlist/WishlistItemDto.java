package practice.internetshop.dto.wishlist;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WishlistItemDto {
    private UUID id;
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private LocalDateTime addedAt;
}
