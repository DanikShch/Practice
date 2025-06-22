package practice.internetshop.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID productId,
            @RequestParam @Min(1) int quantity
    ) {
        return ResponseEntity.ok(cartService.addItem(userDetails, productId, quantity));
    }

    @PutMapping("/items")
    public ResponseEntity<CartDto> updateItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID productId,
            @RequestParam @Min(1) int quantity
    ) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userDetails, productId, quantity));
    }

    @DeleteMapping("/items")
    public ResponseEntity<CartDto> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID productId
    ) {
        return ResponseEntity.ok(cartService.removeItem(userDetails, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails);
        return ResponseEntity.noContent().build();
    }
}