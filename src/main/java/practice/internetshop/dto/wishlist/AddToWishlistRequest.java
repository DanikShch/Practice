package practice.internetshop.dto.wishlist;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddToWishlistRequest {
    @NotNull
    private UUID productId;
}