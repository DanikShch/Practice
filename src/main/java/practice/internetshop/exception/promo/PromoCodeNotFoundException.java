package practice.internetshop.exception.promo;

import java.util.UUID;

public class PromoCodeNotFoundException extends RuntimeException {
    public PromoCodeNotFoundException(UUID id) {
        super("Promo code not found with id: " + id);
    }
    public PromoCodeNotFoundException(String code) {
        super("Promo code not found: " + code);
    }
}
