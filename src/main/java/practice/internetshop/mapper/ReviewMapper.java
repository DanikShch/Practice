package practice.internetshop.mapper;

import practice.internetshop.dto.review.ReviewDto;
import practice.internetshop.dto.review.UpdateReviewRequest;
import practice.internetshop.model.ProductReview;

public interface ReviewMapper {
    ReviewDto toDto(ProductReview entity);
    void updateEntity(UpdateReviewRequest dto, ProductReview entity);
}
