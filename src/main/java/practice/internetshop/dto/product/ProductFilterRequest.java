package practice.internetshop.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductFilterRequest {
    private UUID categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private String searchQuery;
    private String sortBy = "name";
    private String direction = "ASC";
}