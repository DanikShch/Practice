package practice.internetshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import practice.internetshop.dto.review.CreateReviewRequest;
import practice.internetshop.dto.review.ProductRatingStats;
import practice.internetshop.dto.review.ReviewDto;
import practice.internetshop.dto.review.UpdateReviewRequest;
import practice.internetshop.service.AuthService;
import practice.internetshop.service.ProductReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService reviewService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @RequestParam UUID productId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateReviewRequest request) {

        UUID userId = authService.getCurrentUserId(userDetails);
        return ResponseEntity.ok(reviewService.createReview(productId, userId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
    
    @GetMapping("/by-product")
    public ResponseEntity<List<ReviewDto>> getReviewsByProduct(
            @RequestParam UUID productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(reviewService.getUserReviews(userId));
    }
    
    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewDto>> getMyReviews(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = authService.getCurrentUserId(userDetails);
        return ResponseEntity.ok(reviewService.getUserReviews(userId));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<ProductRatingStats> getProductRatingStats(
            @RequestParam UUID productId) {
        return ResponseEntity.ok(reviewService.getProductRatingStats(productId));
    }
    
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable UUID reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }
    
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UpdateReviewRequest request) {

        UUID userId = authService.getCurrentUserId(userDetails);
        return ResponseEntity.ok(reviewService.updateReview(reviewId, userId, request));
    }
    
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = authService.getCurrentUserId(userDetails);
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }
}