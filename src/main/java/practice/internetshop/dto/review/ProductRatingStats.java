package practice.internetshop.dto.review;

import lombok.Data;

import java.util.Map;

@Data
public class ProductRatingStats {
    private Double averageRating;
    private Map<Integer, Long> ratingDistribution;
    private Integer totalReviews;
}