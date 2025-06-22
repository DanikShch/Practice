package practice.internetshop.exception.cart;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}