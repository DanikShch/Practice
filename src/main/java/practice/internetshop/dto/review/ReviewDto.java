package practice.internetshop.dto.review;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewDto {
    private UUID id;
    private UUID productId;
    private UUID userId;
    private String userDisplayName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}