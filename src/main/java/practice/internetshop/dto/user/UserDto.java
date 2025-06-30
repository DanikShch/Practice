package practice.internetshop.dto.user;

import lombok.Data;
import practice.internetshop.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private User.Role role;
    private LocalDateTime createdAt;
    private Integer ordersCount;
    private Integer reviewsCount;
    private boolean hasActiveResetToken;
}
