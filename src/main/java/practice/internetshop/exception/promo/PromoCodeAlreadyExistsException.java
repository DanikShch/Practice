package practice.internetshop.exception.promo;

public class PromoCodeAlreadyExistsException extends RuntimeException {
    public PromoCodeAlreadyExistsException(String code) {
        super("Promo code already exists: " + code);
    }
}