package practice.internetshop.dto.promo;

import lombok.Data;
import practice.internetshop.model.PromoCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PromoCodeDto {
    private UUID id;
    private String code;
    private BigDecimal discountValue;
    private PromoCode.DiscountType discountType;
    private LocalDateTime expiryDate;
    private Boolean isActive;
    private Integer usageLimit;
    private Integer timesUsed;
    private String description;
    private LocalDateTime createdAt;
}