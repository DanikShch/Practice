package practice.internetshop.exception.review;

import java.util.UUID;

public class DuplicateReviewException extends RuntimeException {
    public DuplicateReviewException(UUID userId, UUID productId) {
        super("User " + userId + " has already reviewed product " + productId);
    }
}