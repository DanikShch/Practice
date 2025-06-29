package practice.internetshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import practice.internetshop.dto.wishlist.AddToWishlistRequest;
import practice.internetshop.dto.wishlist.WishlistItemDto;
import practice.internetshop.service.AuthService;
import practice.internetshop.service.WishlistService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<WishlistItemDto> addToWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid AddToWishlistRequest request) {

        UUID userId = authService.getCurrentUserId(userDetails);
        return ResponseEntity.ok(wishlistService.addToWishlist(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemDto>> getWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = authService.getCurrentUserId(userDetails);
        return ResponseEntity.ok(wishlistService.getUserWishlist(userId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID productId) {

        UUID userId = authService.getCurrentUserId(userDetails);
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getWishlistItemCount(
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = authService.getCurrentUserId(userDetails);
        return ResponseEntity.ok(wishlistService.getWishlistItemCount(userId));
    }
}