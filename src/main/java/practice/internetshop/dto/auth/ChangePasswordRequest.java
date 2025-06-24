package practice.internetshop.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Password cannot be empty")
    private String Password;

    @NotBlank(message = "New password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;

    @NotBlank(message = "Password confirmation cannot be empty")
    private String confirmPassword;
}
