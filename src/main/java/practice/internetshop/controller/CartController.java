package practice.internetshop.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.internetshop.dto.cart.CartDto;
import practice.internetshop.model.User;
import practice.internetshop.service.CartService;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam UUID productId,
            @RequestParam(defaultValue = "1") @Positive Integer quantity
    ) {
        return ResponseEntity.ok(cartService.addToCart(user, productId, quantity));
    }

    @PutMapping("/update")
    public ResponseEntity<CartDto> updateCartItem(
            @AuthenticationPrincipal User user,
            @RequestParam UUID productId,
            @RequestParam @Positive Integer quantity
    ) {
        return ResponseEntity.ok(cartService.updateCartItem(user, productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDto> removeFromCart(
            @AuthenticationPrincipal User user,
            @RequestParam UUID productId
    ) {
        return ResponseEntity.ok(cartService.removeFromCart(user, productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}