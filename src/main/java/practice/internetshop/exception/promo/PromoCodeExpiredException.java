package practice.internetshop.exception.promo;

public class PromoCodeExpiredException extends RuntimeException {
    public PromoCodeExpiredException(String code) {
        super("Promo code has expired: " + code);
    }
}