package practice.internetshop.exception.promo;

public class PromoCodeInvalidException extends RuntimeException {
    public PromoCodeInvalidException(String code) {
        super("Promo code is invalid: " + code);
    }
}