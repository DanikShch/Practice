package practice.internetshop.mapper;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.review.ReviewDto;
import practice.internetshop.dto.review.UpdateReviewRequest;
import practice.internetshop.model.ProductReview;
import practice.internetshop.model.User;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDto toDto(ProductReview entity) {
        if (entity == null) {
            return null;
        }

        ReviewDto dto = new ReviewDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setUserDisplayName(getUserDisplayName(entity.getUser()));
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    private String getUserDisplayName(User user) {
        if (user == null) {
            return "Anonymous";
        }
        String email = user.getEmail();
        int atIndex = email.indexOf('@');
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }

    @Override
    public void updateEntity(UpdateReviewRequest dto, ProductReview entity) {
        if (dto.getRating() != null) {
            entity.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
    }
}