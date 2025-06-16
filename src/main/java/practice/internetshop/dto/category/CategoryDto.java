package practice.internetshop.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDto {
    private UUID id;

    @NotBlank(message = "Category name is required")
    private String name;

    private UUID parentId;
}