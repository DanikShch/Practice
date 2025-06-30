package practice.internetshop.mapper;

import practice.internetshop.dto.user.UserDto;
import practice.internetshop.model.User;

public interface UserMapper {
    UserDto toDto(User entity);
}
