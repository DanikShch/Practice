package practice.internetshop.mapper;

import practice.internetshop.dto.promo.PromoCodeDto;
import practice.internetshop.model.PromoCode;

import java.math.BigDecimal;

public interface PromoCodeMapper {
    PromoCodeDto toDto(PromoCode entity);
    PromoCode toEntity(PromoCodeDto dto);
    void updateEntity(PromoCodeDto dto, PromoCode entity);
    BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal amount);
}