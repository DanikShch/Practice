package practice.internetshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.internetshop.dto.cart.CartDto;
import practice.internetshop.service.SessionCartService;

import java.util.UUID;

@RestController
@RequestMapping("/api/session-cart")
@RequiredArgsConstructor
@Validated
public class SessionCartController {
    private final SessionCartService sessionCartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(HttpSession session) {
        return ResponseEntity.ok(sessionCartService.getCart(session));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(
            HttpSession session,
            @RequestParam UUID productId,
            @RequestParam @Min(1) int quantity
    ) {
        return ResponseEntity.ok(sessionCartService.addItem(session, productId, quantity));
    }

    @PutMapping("/items")
    public ResponseEntity<CartDto> updateItem(
            HttpSession session,
            @RequestParam UUID productId,
            @RequestParam @Min(1) int quantity
    ) {
        return ResponseEntity.ok(sessionCartService.updateItemQuantity(session, productId, quantity));
    }

    @DeleteMapping("/items")
    public ResponseEntity<CartDto> removeItem(
            HttpSession session,
            @RequestParam UUID productId
    ) {
        return ResponseEntity.ok(sessionCartService.removeItem(session, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(HttpSession session) {
        sessionCartService.clearCart(session);
        return ResponseEntity.noContent().build();
    }
}