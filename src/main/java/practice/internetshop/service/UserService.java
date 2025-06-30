package practice.internetshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.internetshop.dto.user.UserDto;
import practice.internetshop.model.User;
import practice.internetshop.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    public List<UserDto> getUsersByDateRange(LocalDate from, LocalDate to) {
        LocalDateTime startDate = from.atStartOfDay();
        LocalDateTime endDate = to != null ?
                to.plusDays(1).atStartOfDay() :
                LocalDateTime.now();

        return userRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private UserDto convertToDto(User user) {
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

        dto.setHasActiveResetToken(
                user.getResetToken() != null &&
                        user.getResetTokenExpiry() != null &&
                        user.getResetTokenExpiry().isAfter(LocalDateTime.now())
        );

        return dto;
    }
}
