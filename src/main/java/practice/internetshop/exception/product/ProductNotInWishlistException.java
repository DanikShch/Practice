package practice.internetshop.exception.product;

import java.util.UUID;

public class ProductNotInWishlistException extends RuntimeException {
    public ProductNotInWishlistException(UUID productId) {
        super("Product " + productId + " not found in wishlist");
    }
}