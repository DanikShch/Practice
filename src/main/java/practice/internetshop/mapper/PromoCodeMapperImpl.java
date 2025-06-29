package practice.internetshop.mapper;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.promo.PromoCodeDto;
import practice.internetshop.model.PromoCode;

import java.math.BigDecimal;

@Component
public class PromoCodeMapperImpl implements PromoCodeMapper {

    @Override
    public PromoCodeDto toDto(PromoCode entity) {
        if (entity == null) {
            return null;
        }

        PromoCodeDto dto = new PromoCodeDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setDiscountType(entity.getDiscountType());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setIsActive(entity.getIsActive());
        dto.setUsageLimit(entity.getUsageLimit());
        dto.setTimesUsed(entity.getTimesUsed());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    @Override
    public PromoCode toEntity(PromoCodeDto dto) {
        if (dto == null) {
            return null;
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(dto.getCode());
        promoCode.setDiscountValue(dto.getDiscountValue());
        promoCode.setDiscountType(dto.getDiscountType());
        promoCode.setExpiryDate(dto.getExpiryDate());
        promoCode.setIsActive(dto.getIsActive());
        promoCode.setUsageLimit(dto.getUsageLimit());
        promoCode.setDescription(dto.getDescription());
        return promoCode;
    }

    @Override
    public void updateEntity(PromoCodeDto dto, PromoCode entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setCode(dto.getCode());
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setDiscountType(dto.getDiscountType());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setIsActive(dto.getIsActive());
        entity.setUsageLimit(dto.getUsageLimit());
        entity.setDescription(dto.getDescription());
    }

    @Override
    public BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal amount) {
        if (promoCode == null || amount == null || !promoCode.isValid()) {
            return BigDecimal.ZERO;
        }

        return promoCode.getDiscountType() == PromoCode.DiscountType.PERCENTAGE
                ? amount.multiply(promoCode.getDiscountValue().divide(BigDecimal.valueOf(100)))
                : promoCode.getDiscountValue();
    }
}