package practice.internetshop.mapper.impl;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.user.UserDto;
import practice.internetshop.mapper.UserMapper;
import practice.internetshop.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getOrders() != null) {
            dto.setOrdersCount(user.getOrders().size());
        } else {
            dto.setOrdersCount(0);
        }

        if (user.getReviews() != null) {
            dto.setReviewsCount(user.getReviews().size());
        } else {
            dto.setReviewsCount(0);
        }
        return dto;
    }
}
