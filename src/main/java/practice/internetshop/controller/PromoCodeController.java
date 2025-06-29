package practice.internetshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.internetshop.dto.promo.PromoCodeDto;
import practice.internetshop.service.PromoCodeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/promo-codes")
@RequiredArgsConstructor
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @PostMapping
    public ResponseEntity<PromoCodeDto> createPromoCode(@RequestBody @Valid PromoCodeDto dto) {
        return ResponseEntity.ok(promoCodeService.createPromoCode(dto));
    }

    @GetMapping
    public ResponseEntity<List<PromoCodeDto>> getAllPromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllPromoCodes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCodeDto> getPromoCode(@PathVariable UUID id) {
        return ResponseEntity.ok(promoCodeService.getPromoCodeById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PromoCodeDto> getPromoCodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(promoCodeService.getPromoCodeByCode(code));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromoCodeDto>> getActivePromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllActivePromoCodes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoCodeDto> updatePromoCode(
            @PathVariable UUID id,
            @RequestBody @Valid PromoCodeDto dto) {
        return ResponseEntity.ok(promoCodeService.updatePromoCode(id, dto));
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePromoCode(@PathVariable UUID id) {
        promoCodeService.deactivatePromoCode(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromoCode(@PathVariable UUID id) {
        promoCodeService.deletePromoCode(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/apply")
    public ResponseEntity<BigDecimal> applyPromoCode(
            @RequestParam String code,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(promoCodeService.applyPromoCode(code, amount));
    }
}