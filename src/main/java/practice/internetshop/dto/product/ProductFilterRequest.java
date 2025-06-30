package practice.internetshop.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductFilterRequest {
    private UUID categoryId;

    @DecimalMin("0.00")
    private BigDecimal minPrice;

    @DecimalMin("0.00")
    private BigDecimal maxPrice;

    @Min(0)
    private Integer minStock;

    @Size(max = 100)
    private String searchQuery;

    private String sortBy = "name";

    private Sort.Direction direction = Sort.Direction.ASC;

    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null;
    }

    public boolean hasCategoryFilter() {
        return categoryId != null;
    }
}