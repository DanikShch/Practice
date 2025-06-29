package practice.internetshop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.review.CreateReviewRequest;
import practice.internetshop.dto.review.ProductRatingStats;
import practice.internetshop.dto.review.ReviewDto;
import practice.internetshop.dto.review.UpdateReviewRequest;
import practice.internetshop.exception.review.DuplicateReviewException;
import practice.internetshop.exception.product.ProductNotFoundException;
import practice.internetshop.exception.review.ReviewNotFoundException;
import practice.internetshop.exception.user.UserNotFoundException;
import practice.internetshop.mapper.ReviewMapper;
import practice.internetshop.model.Product;
import practice.internetshop.model.ProductReview;
import practice.internetshop.model.User;
import practice.internetshop.repository.ProductRepository;
import practice.internetshop.repository.ProductReviewRepository;
import practice.internetshop.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDto createReview(UUID productId, UUID userId, CreateReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (reviewRepository.existsByProductIdAndUserId(productId, userId)) {
            throw new DuplicateReviewException(userId, productId);
        }

        ProductReview review = ProductReview.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getProductReviews(UUID productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductRatingStats getProductRatingStats(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        ProductRatingStats stats = new ProductRatingStats();

        Double avgRating = reviewRepository.getAverageRatingByProductId(productId)
                .orElse(0.0);
        stats.setAverageRating(Math.round(avgRating * 10) / 10.0);

        Map<Integer, Long> distribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, 0L);
        }

        reviewRepository.getRatingDistribution(productId)
                .forEach(obj -> distribution.put((Integer) obj[0], (Long) obj[1]));
        stats.setRatingDistribution(distribution);

        stats.setTotalReviews(reviewRepository.countByProductId(productId));

        return stats;
    }

    @Transactional
    public void deleteReview(UUID reviewId, UUID userId) {
        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewDto updateReview(UUID reviewId, UUID userId, UpdateReviewRequest request) {
        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("You can only update your own reviews");
        }

        reviewMapper.updateEntity(request, review);
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getUserReviews(UUID userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewById(UUID reviewId) {
        return reviewMapper.toDto(reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId)));
    }
}