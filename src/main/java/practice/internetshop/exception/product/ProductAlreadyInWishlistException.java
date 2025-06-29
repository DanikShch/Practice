package practice.internetshop.exception.product;

import java.util.UUID;

public class ProductAlreadyInWishlistException extends RuntimeException {
    public ProductAlreadyInWishlistException(UUID productId) {
        super("Product " + productId + " already in wishlist");
    }
}
