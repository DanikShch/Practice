package practice.internetshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.promo.PromoCodeDto;
import practice.internetshop.exception.promo.PromoCodeAlreadyExistsException;
import practice.internetshop.exception.promo.PromoCodeExpiredException;
import practice.internetshop.exception.promo.PromoCodeInvalidException;
import practice.internetshop.exception.promo.PromoCodeNotFoundException;
import practice.internetshop.mapper.PromoCodeMapper;
import practice.internetshop.model.PromoCode;
import practice.internetshop.repository.PromoCodeRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    @Transactional
    public PromoCodeDto createPromoCode(PromoCodeDto dto) {
        if (promoCodeRepository.existsByCode(dto.getCode())) {
            throw new PromoCodeAlreadyExistsException(dto.getCode());
        }

        PromoCode promoCode = promoCodeMapper.toEntity(dto);
        promoCode = promoCodeRepository.save(promoCode);
        return promoCodeMapper.toDto(promoCode);
    }

    @Transactional(readOnly = true)
    public PromoCodeDto getPromoCodeById(UUID id) {
        return promoCodeMapper.toDto(findById(id));
    }

    @Transactional(readOnly = true)
    public PromoCodeDto getPromoCodeByCode(String code) {
        return promoCodeMapper.toDto(findByCode(code));
    }

    @Transactional(readOnly = true)
    public List<PromoCodeDto> getAllActivePromoCodes() {
        return promoCodeRepository.findAllByIsActiveTrueAndExpiryDateAfterOrExpiryDateIsNull(LocalDateTime.now())
                .stream()
                .map(promoCodeMapper::toDto)
                .toList();
    }

    @Transactional
    public PromoCodeDto updatePromoCode(UUID id, PromoCodeDto dto) {
        PromoCode existing = findById(id);
        promoCodeMapper.updateEntity(dto, existing);
        return promoCodeMapper.toDto(promoCodeRepository.save(existing));
    }

    @Transactional
    public void deactivatePromoCode(UUID id) {
        PromoCode promoCode = findById(id);
        promoCode.setIsActive(false);
        promoCodeRepository.save(promoCode);
    }

    @Transactional
    public BigDecimal applyPromoCode(String code, BigDecimal originalAmount) {
        PromoCode promoCode = findByCode(code);

        if (!promoCode.isValid()) {
            throw new PromoCodeInvalidException(code);
        }

        if (promoCode.getExpiryDate() != null && LocalDateTime.now().isAfter(promoCode.getExpiryDate())) {
            throw new PromoCodeExpiredException(code);
        }

        BigDecimal discountAmount = calculateDiscount(promoCode, originalAmount);
        promoCode.incrementUsage();
        promoCodeRepository.save(promoCode);

        return originalAmount.subtract(discountAmount);
    }

    @Transactional(readOnly = true)
    public List<PromoCodeDto> getAllPromoCodes() {
        return promoCodeRepository.findAll().stream()
                .map(promoCodeMapper::toDto)
                .toList();
    }

    @Transactional
    public void deletePromoCode(UUID id) {
        if (!promoCodeRepository.existsById(id)) {
            throw new PromoCodeNotFoundException(id);
        }
        promoCodeRepository.deleteById(id);
    }

    private BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal amount) {
        return promoCode.getDiscountType() == PromoCode.DiscountType.PERCENTAGE
                ? amount.multiply(promoCode.getDiscountValue().divide(BigDecimal.valueOf(100)))
                : promoCode.getDiscountValue();
    }

    private PromoCode findById(UUID id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() -> new PromoCodeNotFoundException(id));
    }

    private PromoCode findByCode(String code) {
        return promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new PromoCodeNotFoundException(code));
    }
}